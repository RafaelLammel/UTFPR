using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using CompartilhamentoNoticias.Models;

namespace CompartilhamentoNoticias
{
    class Program
    {
        private static string Nome;
        private static Assinatura Assin;
        private static UdpClient MulticastSocket;
        private static UdpClient socket;
        private static List<No> Nos;
        private static List<Noticia> Noticias;
        private static IPAddress Group;
        private static int Port;
        private static int IdAtualNoticas;

        static void Main(string[] args)
        {
            // Inicializando items e pedindo o nome do nó
            Assin = new Assinatura();
            Nos = new List<No>();
            Noticias = new List<Noticia>();
            IdAtualNoticas = 1;
            Port = 6789;
            Console.Write("Seja bem vindo ao noticias peer to peer!\nPor favor, entre com seu nome: ");
            Nome = Console.ReadLine();
            try
            {
                // Inicializando socket Unicast, Multicast e definindo endereço de Multicast
                socket = new UdpClient(0);
                MulticastSocket = new UdpClient();
                Group = IPAddress.Parse("228.5.6.7");

                // Atribuindo socket Multicast na porta Port e liberando reuso da porta para outros Sockets multicast
                MulticastSocket.Client.SetSocketOption(SocketOptionLevel.Socket, SocketOptionName.ReuseAddress, true);
                MulticastSocket.Client.Bind(new IPEndPoint(IPAddress.Any, Port));

                // Entra no grupo e serializa as informações do nó em JSON
                // escolhemos essa estratégia por ser simples de serializar e desserializar JSON em objetos C#
                MulticastSocket.JoinMulticastGroup(Group);
                var teste = Assin.ChavePublica.ToString();
                string serialized = JsonSerializer.Serialize(new Entrada() 
                {
                    Nome = Nome,
                    ChavePublica = Assin.ChavePublica,
                    EndPoint = socket.Client.LocalEndPoint.ToString()
                });
                byte[] msgEmBytes = Encoding.UTF8.GetBytes(serialized);
                List<byte> msg = new List<byte>();

                // Além do datagrama de entrada do nó, adicionamos um último bit que serve para identificar
                // o que aquele datagrama faz
                msg.AddRange(msgEmBytes);
                msg.Add(0);

                // Envio do datagrama para o grupo multicast
                MulticastSocket.Send(msg.ToArray(), msg.Count, Group.ToString(), Port);

                // Cria uma Task que roda em paralelo com o programa principal,
                // para receber mensagens do grupo Multicast e outra para Unicast
                Task.Run(RecebeMensagem);
                Task.Run(RecebeUnicast);

                // Thread principal vai cuidar da interação com usuário
                while (true)
                {
                    // Menu com escolhas
                    Console.WriteLine("\nSelecione uma das opções:");
                    Console.WriteLine("1 - Enviar notícia");
                    Console.WriteLine("2 - Visualizar notícias");
                    Console.WriteLine("3 - Avaliar notícia como falsa");
                    string opcao = Console.ReadLine();
                    switch (opcao)
                    {
                        // Enviar notícia
                        case "1":
                            Console.WriteLine("Entre com a notícia: ");
                            string noticia = Console.ReadLine();
                            EnviarNoticia(noticia);
                            break;
                        // Exibir notícias
                        case "2":
                            Console.WriteLine();
                            Noticias.ForEach(x => Console.WriteLine(x.Id + "       " + x.Texto + "     " + x.Autor + "      " + x.VotosFalso.Count));
                            Console.WriteLine();
                            break;
                        // Avaliar notícia como falso:
                        case "3":
                            Console.Write("Entre com o identificador da notícia: ");
                            try
                            {
                                int idNoticiaFalsa = int.Parse(Console.ReadLine());
                                Noticia noticiaFalsa = Noticias.FirstOrDefault(x => x.Id == idNoticiaFalsa);
                                if(noticiaFalsa != null)
                                {
                                    if (noticiaFalsa.VotosFalso.FirstOrDefault(x => x == Nome) == null)
                                        EnviaFalso(noticiaFalsa, Nome);
                                    else
                                        Console.WriteLine("Você já avaliou essa notícia como falsa!");
                                }
                                else
                                {
                                    Console.WriteLine("Notícia não encontrada!");
                                }
                            }
                            catch(Exception e)
                            {
                                Console.WriteLine("Insira um valor válido\n");
                            }
                            break;
                        default:
                            Console.WriteLine("Opção inválida!");
                            break;
                    }
                }
            }
            catch(Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }

        private static void RecebeMensagem()
        {
            // O método Recieve do UdpClient precisa conhecer o endereço do remetente que está esperando,
            // com o Endpoint abaixo ele espera e recebe de qualquer remetente
            IPEndPoint endpoint = new IPEndPoint(IPAddress.Any, 0);

            while (true)
            {
                // Espera receber uma mensagem
                byte[] mensagemIn = MulticastSocket.Receive(ref endpoint);
                
                // Verifica o ultimo byte, que usamos como identificador do tipo do datagrama
                switch (mensagemIn[mensagemIn.Length - 1])
                {
                    // 0 - Nó entrando, é necessário registrar na lista e enviar por Unicast a chave pública deste nó
                    case 0:
                        Entrada novoNo = JsonSerializer.Deserialize<Entrada>(Encoding.UTF8.GetString(mensagemIn.Take(mensagemIn.Length - 1).ToArray()));
                        if(!novoNo.Nome.Equals(Nome)) Console.WriteLine("Nó: " + novoNo.Nome + " se conectou ao grupo multicast!");
                        Nos.Add(new No()
                        {
                            Nome = novoNo.Nome,
                            Chave = novoNo.ChavePublica,
                            Reputacao = 0
                        });
                        EnviaUnicastNovoNo(IPEndPoint.Parse(novoNo.EndPoint));
                        break;
                    // 1 - Recebimento de notícia
                    case 1:
                        Noticia noticia = JsonSerializer.Deserialize<Noticia>(Encoding.UTF8.GetString(mensagemIn.Take(mensagemIn.Length - 1).ToArray()));
                        // Verifica assinatura
                        No autor = Nos.FirstOrDefault(x => x.Nome == noticia.Autor);
                        if (Assin.Validar(noticia.Texto, noticia.Assinatura, autor.Chave)) 
                        {
                            if (Noticias.FirstOrDefault(x => x.Id == noticia.Id) == null)
                            {
                                Noticias.Add(noticia);
                                IdAtualNoticas++;
                            }
                        }
                        break;
                    // 2 - Recebimento de alerta falso
                    case 2:
                        AlertaFalso alerta = JsonSerializer.Deserialize<AlertaFalso>(Encoding.UTF8.GetString(mensagemIn.Take(mensagemIn.Length - 1).ToArray()));
                        Noticia noticiaFalsa = Noticias.FirstOrDefault(x => x.Id == alerta.IdNoticia);
                        noticiaFalsa.VotosFalso.Add(alerta.Alertante);
                        break;
                }
            }
        }

        private static void EnviarNoticia(string msg)
        {
            string serialized = JsonSerializer.Serialize(new Noticia()
            {
                Id = IdAtualNoticas,
                Autor = Nome,
                Texto = msg,
                Assinatura = Assin.Assinar(msg),
                VotosFalso = new List<string>()
            });
            byte[] msgEmBytes = Encoding.UTF8.GetBytes(serialized);
            List<byte> msgB = new List<byte>();
            msgB.AddRange(msgEmBytes);
            msgB.Add(1);
            MulticastSocket.Send(msgB.ToArray(), msgB.Count, Group.ToString(), Port);
        }

        private static void RecebeUnicast()
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
                if (noGrupo.Nome != Nome) Nos.Add(new No()
                {
                    Nome = noGrupo.Nome,
                    Chave = noGrupo.ChavePublica,
                    Reputacao = 0
                });
                if (noGrupo.IdAtualNoticas > IdAtualNoticas) IdAtualNoticas = noGrupo.IdAtualNoticas;
                Console.WriteLine(noGrupo.Nome);
            }
        }

        private static void EnviaUnicastNovoNo(IPEndPoint novoNoEP)
        {
            string datagrama = JsonSerializer.Serialize(new Entrada()
            {
                Nome = Nome,
                ChavePublica = Assin.ChavePublica,
            });
            byte[] msg = Encoding.UTF8.GetBytes(datagrama);
            socket.Send(msg, msg.Length, "127.0.0.1", novoNoEP.Port);
        }

        private static void EnviaFalso(Noticia noticiaFalsa, string nome)
        {
            string serialized = JsonSerializer.Serialize(new AlertaFalso()
            {
                IdNoticia = noticiaFalsa.Id,
                Alertante = nome
            });
            List<byte> msgEmBytes = new List<byte>();
            msgEmBytes.AddRange(Encoding.UTF8.GetBytes(serialized));
            msgEmBytes.Add(2);
            MulticastSocket.Send(msgEmBytes.ToArray(), msgEmBytes.Count, Group.ToString(), Port);
        }
    }
}
