using System.Linq;
using System.Collections.Generic;
using CompartilhamentoNoticias.Models;

namespace CompartilhamentoNoticias
{
    public class Reputacao
    {
        // Recebe uma notícia que foi alertada como falsa e recaucula a reputação do autor
        public void CalculaReputacao(Noticia noticia, List<No> nos, List<Noticia> noticias)
        {
            // Algoritmo de recauculação de reputação
            decimal reputacaoNoticia = (decimal)noticia.VotosFalso.Count / (noticia.QtdNosEnviados - 1);
            noticia.Reputacao = reputacaoNoticia;
            No autorNo = nos.FirstOrDefault(x => x.Nome == noticia.Autor);
            decimal somaReputacao = 0;
            noticias.ForEach(x =>
            {
                somaReputacao += x.Reputacao;
            });
            // Após o calculo, atribuimos a nova reputação ao autor
            autorNo.Reputacao = 1 - (somaReputacao / autorNo.QtdNoticias);
        }
    }
}
