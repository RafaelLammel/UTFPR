package edu.utfpr.servidor.ultrassom.process;

import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class JavaMailApp {
    
    String emailSrc = "coleradodragao19@gmail.com";
    String senha = "seiyadepegasus";
   
    public void sendEmail(String emailDest){
        Properties props = new Properties();
        /** Parâmetros de conexão com servidor Gmail */
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props,
            new javax.mail.Authenticator() {
               @Override
               protected PasswordAuthentication getPasswordAuthentication() 
               {
                    return new PasswordAuthentication(emailSrc, senha);
               }
            });

        /** Ativa Debug para sessão */
        session.setDebug(true);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailSrc)); 
            //Remetente

            Address[] toUser = InternetAddress //Destinatário(s)
                       .parse(emailDest);  

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject("Ultrasom");//Assunto
            
            Multipart mpCorpoPrincipal = new MimeMultipart("related");
            // Criando corpo da mensagem (com texto e html)
            MimeMultipart mpContent = new MimeMultipart("alternative");
            // A raiz para agrupar os dois tipos de textos
            MimeBodyPart corpoRaiz = new MimeBodyPart();
            corpoRaiz.setContent(mpContent);
            // Adiciona a raiz à mensagem
            mpCorpoPrincipal.addBodyPart(corpoRaiz);

            // Adicionando texto puro à raiz
            MimeBodyPart mbpTextPlain = new MimeBodyPart();
            mbpTextPlain.setText("Seu sinal está pronto, agora é só fazer upload amiguinho!\n\nUm abração!!\nDollynho seu amiguinho", "iso-8859-1", "plain");
            mpContent.addBodyPart(mbpTextPlain);
            // Adicionando texto html à raiz
            MimeBodyPart mbpTextHtml = new MimeBodyPart();
            mbpTextHtml.setText("Seu sinal está pronto, agora é só fazer upload amiguinho!<br><br>Um abração!!<br>Dollynho seu amiguinho<br><img src=\"cid:img1\">", "iso-8859-1", "html");
            mpContent.addBodyPart(mbpTextHtml);
            
            // Obtém a referência ao arquivo de imagem
            File f = new File("./email.jpg");
            // Cria a parte de e-mail que irá armazenar a imagem e adiciona o arquivo
            MimeBodyPart mbpImagemInline = new MimeBodyPart();
            mbpImagemInline.setDataHandler(new DataHandler(new FileDataSource(f)));
            mbpImagemInline.setFileName(f.getName());
            // Define um id que pode ser utilizado no html
            mbpImagemInline.setHeader("Content-ID", "<img1>");
            // Insere na mensagem
            mpCorpoPrincipal.addBodyPart(mbpImagemInline);
            //message.setText(msg);
            /**Método para enviar a mensagem criada*/
            message.setContent(mpCorpoPrincipal);
            // Envia a mensagem
            Transport.send(message);

            System.out.println("Email enviado");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
