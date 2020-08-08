using System.Security.Cryptography;
using System.Text;

namespace CompartilhamentoNoticias
{
    public class Assinatura
    {
        public byte[] ChavePublica { get; }
        public RSAParameters ChavePrivada { get; }

        // Gera e armazena as chaves pública/privada
        public Assinatura()
        {
            using(RSACryptoServiceProvider rsa = new RSACryptoServiceProvider())
            {
                ChavePublica = rsa.ExportRSAPublicKey();
                ChavePrivada = rsa.ExportParameters(true);
            }
        }

        public byte[] Assinar(string msg)
        {
            byte[] assinatura;
            using(RSACryptoServiceProvider rsa = new RSACryptoServiceProvider())
            {
                rsa.ImportParameters(ChavePrivada);
                assinatura = rsa.SignData(Encoding.UTF8.GetBytes(msg), new MD5CryptoServiceProvider());
            }
            return assinatura;
        }

        public bool Validar(string msg, byte[] assinatura, byte[] chave)
        {
            using (RSACryptoServiceProvider rsa = new RSACryptoServiceProvider())
            {
                int teste = 0;
                rsa.ImportRSAPublicKey(chave, out teste);
                return rsa.VerifyData(Encoding.UTF8.GetBytes(msg), new MD5CryptoServiceProvider(), assinatura);
            }
        }
    }
}
