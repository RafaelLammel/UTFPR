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

        //Fechando streams e Socket
        os.close();
        br.close();
        socket.close();
    }

}