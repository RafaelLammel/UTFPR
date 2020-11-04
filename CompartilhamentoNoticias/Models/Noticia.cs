using System.Collections.Generic;

namespace CompartilhamentoNoticias.Models
{
    public class Noticia
    {
        public int Id { get; set; }
        public string Autor { get; set; }
        public string Texto { get; set; }
        public byte[] Assinatura { get; set; }
        public List<string> VotosFalso { get; set; }
        public int QtdNosEnviados { get; set; }
        public decimal Reputacao { get; set; }
    }
}
