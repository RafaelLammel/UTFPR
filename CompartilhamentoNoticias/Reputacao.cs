using System.Linq;
using System.Collections.Generic;
using CompartilhamentoNoticias.Models;

namespace CompartilhamentoNoticias
{
    public class Reputacao
    {
        public void CalculaReputacao(Noticia noticia, List<No> nos, List<Noticia> noticias)
        {
            decimal reputacaoNoticia = (decimal)noticia.VotosFalso.Count / (noticia.QtdNosEnviados - 1);
            noticia.Reputacao = reputacaoNoticia;
            No AutorNo = nos.FirstOrDefault(x => x.Nome == noticia.Autor);
            decimal somaReputacao = 0;
            noticias.ForEach(x =>
            {
                somaReputacao += x.Reputacao;
            });
            AutorNo.Reputacao = 1 - (somaReputacao / AutorNo.QtdNoticias);
        }
    }
}
