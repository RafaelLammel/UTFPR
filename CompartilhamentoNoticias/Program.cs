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
        private static UdpClient multicastSocket;
        private static UdpClient socket;

        static void Main(string[] args)
        {
            // Inicializando items e pedindo o nome do nó
            Assin = new Assinatura();
            Console.Write("Seja bem vindo ao noticias peer to peer!\nPor favor, entre com seu nome: ");
            Nome = Console.ReadLine();
            try
            {
                // Inicializando socket Unicast, Multicast e definindo endereço de Multicast
                socket = new UdpClient(0);
                multicastSocket = new UdpClient();
                IPAddress group = IPAddress.Parse("228.5.6.7");

                // Atribuindo socket Multicast na porta 6789 e liberando reuso da porta para outros Sockets multicast
                multicastSocket.Client.SetSocketOption(SocketOptionLevel.Socket, SocketOptionName.ReuseAddress, true);
                multicastSocket.Client.Bind(new IPEndPoint(IPAddress.Any, 6789));

                // Entra no grupo e serializa as informações do nó em JSON
                // escolhemos essa estratégia por ser simples de serializar e desserializar JSON em objetos C#
                multicastSocket.JoinMulticastGroup(group);
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
                multicastSocket.Send(msg.ToArray(), msg.Count, group.ToString(), 6789);

                // Cria uma Task que roda em paralelo com o programa principal,
                // para receber mensagens do grupo Multicast e outra para Unicast
                Task.Run(RecebeMensagem);
                Task.Run(RecebeUnicast);
                Console.ReadLine();
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
                byte[] mensagemIn = multicastSocket.Receive(ref endpoint);
                
                // Verifica o ultimo byte, que usamos como identificador do tipo do datagrama
                switch (mensagemIn[mensagemIn.Length - 1])
                {
                    // 0 - Nó entrando, é necessário registrar na lista e enviar por Unicast a chave pública deste nó
                    case 0:
                        Entrada novoNo = JsonSerializer.Deserialize<Entrada>(Encoding.UTF8.GetString(mensagemIn.Take(mensagemIn.Length - 1).ToArray()));
                        if(!novoNo.Nome.Equals(Nome)) Console.WriteLine("Nó: " + novoNo.Nome + " se conectou ao grupo multicast!");
                        EnviaUnicastNovoNo(IPEndPoint.Parse(novoNo.EndPoint));
                        break;
                }
            }
        }

        private static void RecebeUnicast()
        {
            // O método Recieve do UdpClient precisa conhecer o endereço do remetente que está esperando,
            // com o Endpoint abaixo ele espera e recebe de qualquer remetente
            IPEndPoint endpoint = new IPEndPoint(IPAddress.Any, 0);

            Console.WriteLine("Nós conectados (incluido o próprio nó): ");

            while (true)
            {
                // Espera receber uma mensagem
                byte[] mensagemIn = socket.Receive(ref endpoint);
                Entrada noGrupo = JsonSerializer.Deserialize<Entrada>(Encoding.UTF8.GetString(mensagemIn));
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

    }
}
