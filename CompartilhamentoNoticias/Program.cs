using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Text.Json;
using System.Threading;
using System.Threading.Tasks;
using CompartilhamentoNoticias.Models;

namespace CompartilhamentoNoticias
{
    class Program
    {
        static void Main(string[] args)
        {
            // Inicializando items e pedindo o nome do nó
            ComunicacaoSockets com = new ComunicacaoSockets();
            Assinatura assin = new Assinatura();
            List<No> nos = new List<No>();
            List<Noticia> noticias = new List<Noticia>();
            int idAtualNoticias = 1;
            int porta = 6789;
            Console.Write("Seja bem vindo ao noticias peer to peer!\nPor favor, entre com seu nome: ");
            string nome = Console.ReadLine();
            try
            {
                // Inicializando socket Unicast, Multicast e definindo endereço de Multicast
                UdpClient socket = new UdpClient(0);
                UdpClient multicastSocket = new UdpClient();
                IPAddress grupo = IPAddress.Parse("228.5.6.7");

                // Atribuindo socket Multicast na porta porta e liberando reuso da porta para outros Sockets multicast
                multicastSocket.Client.SetSocketOption(SocketOptionLevel.Socket, SocketOptionName.ReuseAddress, true);
                multicastSocket.Client.Bind(new IPEndPoint(IPAddress.Any, porta));

                // Entra no grupo e serializa as informações do nó em String/JSON
                // escolhemos essa estratégia por ser simples de serializar e desserializar JSON em objetos C#
                multicastSocket.JoinMulticastGroup(grupo);
                var teste = assin.ChavePublica.ToString();
                string serialized = JsonSerializer.Serialize(new Entrada()
                {
                    Nome = nome,
                    ChavePublica = assin.ChavePublica,
                    EndPoint = socket.Client.LocalEndPoint.ToString()
                });
                byte[] msgEmBytes = Encoding.UTF8.GetBytes(serialized);
                List<byte> msg = new List<byte>();

                // Além da mensagem de entrada do nó, adicionamos um último bit que serve para identificar
                // o que aquela mensagem faz
                msg.AddRange(msgEmBytes);
                msg.Add(0);

                // Cria uma Task que roda em paralelo com o programa principal,
                // para receber mensagens do grupo Multicast e outra para Unicast
                CancellationTokenSource cancelaMulticast = new CancellationTokenSource();
                CancellationTokenSource cancelaUnicast = new CancellationTokenSource();
                Task.Run(() => { com.RecebeMensagem(nome, multicastSocket, nos, assin, noticias, idAtualNoticias, socket); }, cancelaMulticast.Token);
                Task.Run(() => { com.RecebeUnicast(socket, nos, idAtualNoticias); }, cancelaUnicast.Token);

                // Envio do datagrama para o grupo multicast
                multicastSocket.Send(msg.ToArray(), msg.Count, grupo.ToString(), porta);

                // Um evento que dispara quando CTRL+C é pressionado
                Console.CancelKeyPress += new ConsoleCancelEventHandler((object sender, ConsoleCancelEventArgs args) => 
                {
                    EncerraPrograma(multicastSocket, socket, grupo, porta, nome, com, cancelaMulticast, cancelaUnicast);
                });

                // Thread principal vai cuidar da interação com usuário
                while (true)
                {
                    // Menu com escolhas
                    Console.WriteLine("\nSelecione uma das opções:");
                    Console.WriteLine("1 - Enviar notícia");
                    Console.WriteLine("2 - Visualizar notícias");
                    Console.WriteLine("3 - Avaliar notícia como falsa");
                    Console.WriteLine("4 - Listar nós conectados");
                    string opcao = Console.ReadLine();
                    switch (opcao)
                    {
                        // Enviar notícia
                        case "1":
                            Console.Write("\nEntre com a notícia: ");
                            string noticia = Console.ReadLine();
                            com.EnviarNoticia(noticia, idAtualNoticias, nome, assin, nos, grupo, porta, multicastSocket);
                            break;
                        // Exibir notícias
                        case "2":
                            Console.WriteLine("\n{0} | {1} | {2} | Votos Falsos", "ID".PadRight(3), "Notícia".PadRight(30), "Autor".PadRight(10));
                            noticias.ForEach(x => Console.WriteLine("{0} | {1} | {2} | {3}", x.Id.ToString().PadRight(3), x.Texto.PadRight(30), x.Autor.PadRight(10), x.VotosFalso.Count));
                            break;
                        // Avaliar notícia como falso:
                        case "3":
                            Console.Write("\nEntre com o identificador da notícia: ");
                            // O try catch foi adicionado no caso do usuário não inserir um número inteiro
                            try
                            {
                                int idNoticiaFalsa = int.Parse(Console.ReadLine());
                                Noticia noticiaFalsa = noticias.FirstOrDefault(x => x.Id == idNoticiaFalsa);
                                if (noticiaFalsa != null)
                                {
                                    if (noticiaFalsa.Autor == nome)
                                    {
                                        Console.WriteLine("Não é possível avaliar a própria notícia!");
                                    }
                                    else if (noticiaFalsa.VotosFalso.FirstOrDefault(x => x == nome) == null)
                                        com.EnviaFalso(noticiaFalsa, nome, multicastSocket, grupo, porta);
                                    else
                                        Console.WriteLine("Você já avaliou essa notícia como falsa!");
                                }
                                else
                                {
                                    Console.WriteLine("Notícia não encontrada!");
                                }
                            }
                            catch (Exception)
                            {
                                Console.WriteLine("Insira um valor válido");
                            }
                            break;
                        // Exibir a reputação dos nós conectados
                        case "4":
                            Console.WriteLine("\n{0} | Reputação*", "Nó".PadRight(10));
                            nos.ForEach(x => Console.WriteLine("{0} | {1:P2}", x.Nome.PadRight(10), x.Reputacao));
                            Console.WriteLine("\n*Quanto maior, mais confiável");
                            break;
                        // Para qualquer outra coisa digitada, portanto inválida
                        default:
                            Console.WriteLine("Opção inválida!");
                            break;
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }

        // Função que encerra os sockets antes de finalizar o programa
        private static void EncerraPrograma(
            UdpClient multicastSocket,
            UdpClient socket,
            IPAddress grupo,
            int porta,
            string nome,
            ComunicacaoSockets com,
            CancellationTokenSource cancelaMulticast,
            CancellationTokenSource cancelaUnicast
        )
        {
            cancelaMulticast.Cancel();
            cancelaUnicast.Cancel();
            com.Desconecta(nome, multicastSocket, grupo, porta);
            multicastSocket.DropMulticastGroup(grupo);
            multicastSocket.Close();
            socket.Close();
            Console.WriteLine("Desconectado!\n");
        }
    }
}
