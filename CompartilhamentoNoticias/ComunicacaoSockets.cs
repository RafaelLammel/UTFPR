using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Text.Json;
using CompartilhamentoNoticias.Models;

namespace CompartilhamentoNoticias
{
    public class ComunicacaoSockets
    {
        // Recebe uma mensagem do grupo multicast e lida com ela baseado no tipo da mensagem
        public void RecebeMensagem(string nome, UdpClient multicastSocket, List<No> nos, Assinatura assin, List<Noticia> noticias, int idAtualNoticas, UdpClient socket)
        {
            // O método Recieve do UdpClient precisa conhecer o endereço do remetente que está esperando,
            // com o Endpoint abaixo ele espera e recebe de qualquer remetente
            IPEndPoint endpoint = new IPEndPoint(IPAddress.Any, 0);

            while (true)
            {
                // Espera receber uma mensagem
                byte[] mensagemIn = multicastSocket.Receive(ref endpoint);

                // Verifica o ultimo byte, que usamos como identificador do tipo da mensagem
                switch (mensagemIn[mensagemIn.Length - 1])
                {
                    // 0 - Nó entrando, é necessário registrar na lista e enviar por Unicast a chave pública deste nó
                    case 0:
                        // Desserializa a mensagem e adiciona o novo nó na lista local
                        Entrada novoNo = JsonSerializer.Deserialize<Entrada>(Encoding.UTF8.GetString(mensagemIn.Take(mensagemIn.Length - 1).ToArray()));
                        if (!novoNo.Nome.Equals(nome)) Console.WriteLine("Nó: " + novoNo.Nome + " se conectou ao grupo multicast!");
                        nos.Add(new No()
                        {
                            Nome = novoNo.Nome,
                            Chave = novoNo.ChavePublica,
                            Reputacao = 1,
                            QtdNoticias = 0
                        });
                        EnviaUnicastNovoNo(IPEndPoint.Parse(novoNo.EndPoint), nome, assin, socket);
                        break;
                    // 1 - Recebimento de notícia
                    case 1:
                        // Desserializa a noticia, verifica a assinatura e registra se a assinatura estiver ok
                        Noticia noticia = JsonSerializer.Deserialize<Noticia>(Encoding.UTF8.GetString(mensagemIn.Take(mensagemIn.Length - 1).ToArray()));
                        // Verifica assinatura
                        No autor = nos.FirstOrDefault(x => x.Nome == noticia.Autor);
                        if (assin.Validar(noticia.Texto, noticia.Assinatura, autor.Chave))
                        {
                            if (noticias.FirstOrDefault(x => x.Id == noticia.Id) == null)
                            {
                                noticias.Add(noticia);
                                idAtualNoticas++;
                                autor.QtdNoticias++;
                            }
                        }
                        break;
                    // 2 - Recebimento de alerta falso
                    case 2:
                        // Desserializa o alerta, pega a notícia pelo ID e faz um novo calculo de reputação para o autor do nó
                        AlertaFalso alerta = JsonSerializer.Deserialize<AlertaFalso>(Encoding.UTF8.GetString(mensagemIn.Take(mensagemIn.Length - 1).ToArray()));
                        Noticia noticiaFalsa = noticias.FirstOrDefault(x => x.Id == alerta.IdNoticia);
                        noticiaFalsa.VotosFalso.Add(alerta.Alertante);
                        Reputacao rep = new Reputacao();
                        rep.CalculaReputacao(noticiaFalsa, nos, noticias);
                        break;
                    // 3 - Um nó vai se desconectar, é preciso remove-lo da lista local
                    case 3:
                        string nomeNo = Encoding.UTF8.GetString(mensagemIn.Take(mensagemIn.Length - 1).ToArray());
                        No noRemover = nos.FirstOrDefault(x => x.Nome == nomeNo);
                        nos.Remove(noRemover);
                        Console.WriteLine($"\nO nó {nomeNo} se desconectou\n");
                        break;
                }
            }
        }

        // Envia uma notícia para o grupo multicast
        public void EnviarNoticia(string msg, int idAtualNoticias, string nome, Assinatura assin, List<No> nos, IPAddress grupo, int porta, UdpClient multicastSocket)
        {
            // Cria e Serializa em String/JSON um objeto notícia
            string serialized = JsonSerializer.Serialize(new Noticia()
            {
                Id = idAtualNoticias,
                Autor = nome,
                Texto = msg,
                Assinatura = assin.Assinar(msg),
                VotosFalso = new List<string>(),
                QtdNosEnviados = nos.Count,
                Reputacao = 0
            });

            // Transforma em um array de bytes e envia a notícia para o grupo
            byte[] msgEmBytes = Encoding.UTF8.GetBytes(serialized);
            List<byte> msgB = new List<byte>();
            msgB.AddRange(msgEmBytes);
            msgB.Add(1);
            multicastSocket.Send(msgB.ToArray(), msgB.Count, grupo.ToString(), porta);
        }

        // Quando o nó entra no grupo, este método lida com os envios de chave pública dos outros nós por meio de Unicast
        public void RecebeUnicast(UdpClient socket, string nome, List<No> nos, int idAtualNoticias)
        {
            // O método Recieve do UdpClient precisa conhecer o endereço do remetente que está esperando,
            // com o Endpoint abaixo ele espera e recebe de qualquer remetente
            IPEndPoint endpoint = new IPEndPoint(IPAddress.Any, 0);

            Console.WriteLine("\nNós conectados (incluido o próprio nó): ");

            while (true)
            {
                // Espera receber uma mensagem
                byte[] mensagemIn = socket.Receive(ref endpoint);

                // Converte a mensagem no objeto de entrada, e insere o nó em uma lista local
                Entrada noGrupo = JsonSerializer.Deserialize<Entrada>(Encoding.UTF8.GetString(mensagemIn));
                if (noGrupo.Nome != nome) nos.Add(new No()
                {
                    Nome = noGrupo.Nome,
                    Chave = noGrupo.ChavePublica,
                    Reputacao = 1
                });
                // Recebe o ID atual das notícias
                if (noGrupo.IdAtualNoticas > idAtualNoticias) idAtualNoticias = noGrupo.IdAtualNoticas;
                Console.WriteLine(noGrupo.Nome);
            }
        }

        // Quando um novo nó entra no grupo, este método deve ser disparado, enviando a chave pública e o nome do nó atual
        public void EnviaUnicastNovoNo(IPEndPoint novoNoEP, string nome, Assinatura assin, UdpClient socket)
        {
            // Cria e serializa um objeto de entrada
            string datagrama = JsonSerializer.Serialize(new Entrada()
            {
                Nome = nome,
                ChavePublica = assin.ChavePublica,
            });

            // Transforma em Array de bytes e envia a mensagem
            byte[] msg = Encoding.UTF8.GetBytes(datagrama);
            socket.Send(msg, msg.Length, "127.0.0.1", novoNoEP.Port);
        }

        // Função disparada quando o nó envia um alerta de notícia falso
        public void EnviaFalso(Noticia noticiaFalsa, string nome, UdpClient multicastSocket, IPAddress grupo, int porta)
        {
            // Cria e serializa em String/JSON um objeto AlertaFalso, contendo informação de quem deu o alerta e qual notícia
            string serialized = JsonSerializer.Serialize(new AlertaFalso()
            {
                IdNoticia = noticiaFalsa.Id,
                Alertante = nome
            });

            // Transforma em Array de bytes e envia a mensagem
            List<byte> msgEmBytes = new List<byte>();
            msgEmBytes.AddRange(Encoding.UTF8.GetBytes(serialized));
            msgEmBytes.Add(2);
            multicastSocket.Send(msgEmBytes.ToArray(), msgEmBytes.Count, grupo.ToString(), porta);
        }

        // Avisa por multicast que o nó atual está saindo do grupo
        public void Desconecta(string nome, UdpClient multicastSocket, IPAddress grupo, int porta)
        {
            List<byte> msgEmBytes = new List<byte>();
            msgEmBytes.AddRange(Encoding.UTF8.GetBytes(nome));
            msgEmBytes.Add(3);
            multicastSocket.Send(msgEmBytes.ToArray(), msgEmBytes.Count, grupo.ToString(), porta);
        }
    }
}
