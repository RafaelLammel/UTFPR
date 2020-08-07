using System.Net;

namespace CompartilhamentoNoticias.Models
{
    public class Entrada
    {
        public string Nome { get; set; }
        public string EndPoint { get; set; }
        public byte[] ChavePublica { get; set; }
    }
}
