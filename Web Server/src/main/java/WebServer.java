import java.io.*;
import java.net.*;
import java.util.*;

public class WebServer {

    public static void main(String argv[]) throws Exception {
        //Definindo a porta
        int port = 6789;

        //Estabelecer um Socket receptor
        ServerSocket server = new ServerSocket(port);

        //Processar requisições HTTP em um loop infinito
        while(true){
            //Ouvir uma requisição de conexão TCP
            Socket receptor = server.accept();

            //Construir um objeto para processar a requisição HTTP
            HttpRequest request = new HttpRequest(receptor);

            //Construir um novo Thread para processar a requisição
            Thread thread = new Thread(request);

            //Iniciar o Thread
            thread.start();
        }
    }

}

final class HttpRequest implements Runnable{

    final static String CRLF = "\r\n";
    Socket socket;

    //Construtor
    public HttpRequest(Socket socket) throws Exception{
        this.socket = socket;
    }

    //Implementar o método run() da interface Runnable
    public void run(){
        try {
            processRequest();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void processRequest() throws Exception{
        //Pegar a referencia do Input e Output das Streams do Socket
        InputStream is = socket.getInputStream();
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());

        //Preparar filtros de Input Stream
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        //Pegar a linha de requisição da mensagem da requisição HTTP
        String requestLine = br.readLine();

        //Mostrar a linha de requisição
        System.out.println();
        System.out.println(requestLine);

        //Pegar e mostrar os Header Lines.
        String headerLine = null;
        while((headerLine = br.readLine()).length() != 0){
            System.out.println(headerLine);
        }

        //Extraindo o filename de requestLine
        StringTokenizer tokens = new StringTokenizer(requestLine);
        tokens.nextToken(); //Pula o método, que assumimos ser GET
        String fileName = tokens.nextToken();

        //Colocar um "." simbolizando que a requisição do arquivo esteja no diretório atual
        fileName = "."+fileName;

        //Abrindo o arquivo requisitado
        FileInputStream fis = null;
        boolean fileExists = true;
        try{
            fis = new FileInputStream(fileName);
        }catch (FileNotFoundException e){
            fileExists = false;
        }

        //Construindo a mensaegem de resposta
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;
        if(fileExists){
            statusLine = "HTTP/1.0 200 OK" + CRLF;
            contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
        }
        else{
            statusLine = "HTTP/1.0 404 Not Found" + CRLF;
            contentTypeLine = "Content-type: text/html" + CRLF;
            entityBody = "<HTML>" + "<HEAD><TITLE>Not Found</TITLE></HEAD>"+"<BODY>Not Found</BODY></HTML>";
        }

        //Mandar a linha de Status
        os.writeBytes(statusLine);

        //Mandar a linha de contentType
        os.writeBytes(contentTypeLine);

        //Mandar a linha vazia para indicar o fim do Header
        os.writeBytes(CRLF);

        //Mandar o corpo entitidade
        if(fileExists){
            sendBytes(fis,os);
            fis.close();
        }
        else{
            os.writeBytes(entityBody);
        }

        //Fechando streams e Socket
        os.close();
        br.close();
        socket.close();
    }

    private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception{
        //Construir um Buffer 1K para segurar os bytes a caminho do socket
        byte[] buffer = new byte[1024];
        int bytes = 0;

        //Copiar o arquivo requisitado para o OutputStram do Socket
        while((bytes = fis.read(buffer)) != -1){
            os.write(buffer, 0, bytes);
        }
    }

    private static String contentType(String fileName){
        if(fileName.endsWith(".htm") || fileName.endsWith(".html")){
            return "text/html";
        }
        if(fileName.endsWith(".gif")){
            return "image/gif";
        }
        if(fileName.endsWith(".jpeg") || fileName.endsWith(".jpg"){
            return "image/jpeg";
        }
        return "application/octet-stream";
    }

}
