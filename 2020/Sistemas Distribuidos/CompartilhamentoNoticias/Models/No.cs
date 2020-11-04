using System.Security.Cryptography;

namespace CompartilhamentoNoticias.Models
{
    public class No
    {
        public string Nome { get; set; }
        public byte[] Chave { get; set; }
        public decimal Reputacao { get; set; }
        public int QtdNoticias { get; set; }
    }
}
