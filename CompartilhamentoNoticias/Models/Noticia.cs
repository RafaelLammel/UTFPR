﻿using System.Collections.Generic;

namespace CompartilhamentoNoticias.Models
{
    class Noticia
    {
        public int Id { get; set; }
        public string Autor { get; set; }
        public string Texto { get; set; }
        public byte[] Assinatura { get; set; }
        public List<string> VotosFalso { get; set; }
    }
}
