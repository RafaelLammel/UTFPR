package utfpr.edu.sisdist.bolsavalores.util;

import java.util.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import utfpr.edu.sisdist.bolsavalores.model.Cliente;
import utfpr.edu.sisdist.bolsavalores.model.Interesse;
import utfpr.edu.sisdist.bolsavalores.model.Transacao;

@Getter
@Setter
public class Utilidades {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private static Utilidades INSTANCIA = null;

    private int idAtual;
    private int idAcaoAtual;
    private Map<Integer, Float> acoes;
    private List<Cliente> clientes;
    private List<Transacao> compras;
    private List<Transacao> vendas;

    private Utilidades() {
        idAcaoAtual = 1;
        idAtual = 1;
        acoes = new HashMap<>();
        clientes = new ArrayList<>();
        compras = new ArrayList<>();
        vendas = new ArrayList<>();
        TimerTask task = new TimerTask() {
            public void run() {
                atualizaValor();
            }
        };
        Timer timer = new Timer("Timer");
        timer.schedule(task, 30000, 30000);
    }

    public static Utilidades getInstance() {
        if (INSTANCIA == null) {
            synchronized (Utilidades.class) {
                if (INSTANCIA == null) {
                    INSTANCIA = new Utilidades();
                }
            }
        }
        return INSTANCIA;
    }

    /**
     * Adiciona ou decrementa um valor aleatório de todas as ações
     */
    private void atualizaValor(){
        int max = 50;
        int min = -50;
        Random gerador = new Random();
        for(Integer acao : acoes.keySet()) {
            float novoValor = acoes.get(acao) + ((float)gerador.nextInt(max-min) + min);
            if(novoValor <= 0) {
                acoes.put(acao, 1f);
            }
            else {
                acoes.put(acao, novoValor);
            }
        }
        notificaMargem();
        //com os novos valores definidos, verificamos se o valor não ultrapassa os
        //limites definidos pelo cliente na lista de interesse
    }

    /**
     * Notifica o cliente caso o valor uma ação ultrapassa o limite de piso e teto definido pelo cliente
     */
    public void notificaMargem(){
        for(Cliente cliente : this.clientes) {
            if(cliente.getInteresses().size() > 0) {
                for(Interesse interesse : cliente.getInteresses()) {
                    float valor = acoes.get(interesse.getId());
                    //atualiza "ultimo valor", para que o cliente não receba notificações repetidas
                    if (interesse.getUltimoValor() != valor) {
                        if(interesse.getPiso() > valor) {
                            simpMessagingTemplate.convertAndSendToUser(String.valueOf(cliente.getId()), "/notifica","\nA ação de Id " + interesse.getId() + " ficou abaixo do seu limite de piso: \n" +
                                    "Limite definido: " + interesse.getPiso() +
                                    " Valor atual: " + valor);
                        }
                        if(interesse.getTeto() < valor) {
                            simpMessagingTemplate.convertAndSendToUser(String.valueOf(cliente.getId()), "/notifica", "\nA ação de Id " + interesse.getId() + " ficou acima do seu limite de teto: \n" +
                                    "Limite definido: " + interesse.getTeto() +
                                    " Valor atual: " + valor);
                        }
                        interesse.setUltimoValor(valor);

                    }
                }
            }
        }
    }

}
