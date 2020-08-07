using System.Security.Cryptography;

namespace CompartilhamentoNoticias
{
    public class Assinatura
    {
        public byte[] ChavePublica { get; }
        public byte[] ChavePrivada { get; }

        // Gera e armazena as chaves pública/privada
        public Assinatura()
        {
            using(RSACryptoServiceProvider rsa = new RSACryptoServiceProvider())
            {
                ChavePublica = rsa.ExportRSAPublicKey();
                ChavePrivada = rsa.ExportRSAPrivateKey();
            }
        }
    }
}
