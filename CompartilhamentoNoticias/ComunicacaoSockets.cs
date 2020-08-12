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
        public void RecebeMensagem(string nome, UdpClient multicastSocket, List<No> nos, Assinatura assin, List<Noticia> noticias, int idAtualNoticas, UdpClient socket)
        {
            // O método Recieve do UdpClient precisa conhecer o endereço do remetente que está esperando,
            // com o Endpoint abaixo ele espera e recebe de qualquer remetente
            IPEndPoint endpoint = new IPEndPoint(IPAddress.Any, 0);

            while (true)
            {
                // Espera receber uma mensagem
                byte[] mensagemIn = multicastSocket.Receive(ref endpoint);

                // Verifica o ultimo byte, que usamos como identificador do tipo do datagrama
                switch (mensagemIn[mensagemIn.Length - 1])
                {
                    // 0 - Nó entrando, é necessário registrar na lista e enviar por Unicast a chave pública deste nó
                    case 0:
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
                        AlertaFalso alerta = JsonSerializer.Deserialize<AlertaFalso>(Encoding.UTF8.GetString(mensagemIn.Take(mensagemIn.Length - 1).ToArray()));
                        Noticia noticiaFalsa = noticias.FirstOrDefault(x => x.Id == alerta.IdNoticia);
                        noticiaFalsa.VotosFalso.Add(alerta.Alertante);
                        Reputacao rep = new Reputacao();
                        rep.CalculaReputacao(noticiaFalsa, nos, noticias);
                        break;
                }
            }
        }

        public void EnviarNoticia(string msg, int idAtualNoticias, string nome, Assinatura assin, List<No> nos, IPAddress grupo, int porta, UdpClient multicastSocket)
        {
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
            byte[] msgEmBytes = Encoding.UTF8.GetBytes(serialized);
            List<byte> msgB = new List<byte>();
            msgB.AddRange(msgEmBytes);
            msgB.Add(1);
            multicastSocket.Send(msgB.ToArray(), msgB.Count, grupo.ToString(), porta);
        }

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
                Entrada noGrupo = JsonSerializer.Deserialize<Entrada>(Encoding.UTF8.GetString(mensagemIn));
                if (noGrupo.Nome != nome) nos.Add(new No()
                {
                    Nome = noGrupo.Nome,
                    Chave = noGrupo.ChavePublica,
                    Reputacao = 1
                });
                if (noGrupo.IdAtualNoticas > idAtualNoticias) idAtualNoticias = noGrupo.IdAtualNoticas;
                Console.WriteLine(noGrupo.Nome);
            }
        }

        public void EnviaUnicastNovoNo(IPEndPoint novoNoEP, string nome, Assinatura assin, UdpClient socket)
        {
            string datagrama = JsonSerializer.Serialize(new Entrada()
            {
                Nome = nome,
                ChavePublica = assin.ChavePublica,
            });
            byte[] msg = Encoding.UTF8.GetBytes(datagrama);
            socket.Send(msg, msg.Length, "127.0.0.1", novoNoEP.Port);
        }

        public void EnviaFalso(Noticia noticiaFalsa, string nome, UdpClient multicastSocket, IPAddress grupo, int porta)
        {
            string serialized = JsonSerializer.Serialize(new AlertaFalso()
            {
                IdNoticia = noticiaFalsa.Id,
                Alertante = nome
            });
            List<byte> msgEmBytes = new List<byte>();
            msgEmBytes.AddRange(Encoding.UTF8.GetBytes(serialized));
            msgEmBytes.Add(2);
            multicastSocket.Send(msgEmBytes.ToArray(), msgEmBytes.Count, grupo.ToString(), porta);
        }
    }
}
