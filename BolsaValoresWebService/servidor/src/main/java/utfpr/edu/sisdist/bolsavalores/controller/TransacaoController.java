package utfpr.edu.sisdist.bolsavalores.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import utfpr.edu.sisdist.bolsavalores.model.Acao;
import utfpr.edu.sisdist.bolsavalores.model.Cliente;
import utfpr.edu.sisdist.bolsavalores.model.Transacao;
import utfpr.edu.sisdist.bolsavalores.util.Utilidades;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

@RestController
@CrossOrigin(origins = "*")
public class TransacaoController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("compra")
    public void compra(@RequestBody Transacao compra) {
        Utilidades.getInstance().getCompras().add(compra);
        for(Transacao venda : Utilidades.getInstance().getVendas()) {
            // Com a lista de vendas, realiza as verificações necessárias para
            // ver se existe um par igual
            if(compra.getIdCliente() != venda.getIdCliente() &&
                    compra.getPreco() == venda.getPreco() &&
                    compra.getId() == venda.getId() &&
                    compra.getQtd() == venda.getQtd()) {
                Optional<Cliente> comprador = Utilidades.getInstance().getClientes().stream()
                        .filter(x -> x.getId() == compra.getIdCliente()).findFirst();
                Optional<Cliente> vendedor = Utilidades.getInstance().getClientes().stream()
                        .filter(x -> x.getId() == venda.getIdCliente()).findFirst();
                // Se existir, realiza a compra/venda para o par
                // e adiciona a ação na lista de cotações do comprador.
                // Também dispara uma notificação aos dois envolvidos do par para
                // sinalizar a conclusão do processo.
                if(comprador.isPresent() && vendedor.isPresent()) {
                    comprador.get().addAcao(compra.getId(), compra.getQtd());
                    vendedor.get().removeAcao(compra.getId(), compra.getQtd());
                    this.simpMessagingTemplate.convertAndSendToUser(String.valueOf(vendedor.get().getId()), "/notifica","Venda da ação " + venda.getId() + " quantidade: " + venda.getQtd() + " Preço: " + venda.getPreco() + " efetuada com sucesso!");
                    this.simpMessagingTemplate.convertAndSendToUser(String.valueOf(comprador.get().getId()), "/notifica", "Compra da ação " + compra.getId() + " quantidade: " + compra.getQtd() + " Preço: " + compra.getPreco() + " efetuada com sucesso!");
                    comprador.get().getCotacoes().add(compra.getId()); // adicionando a compra na lista de cotações do cliente
                    Utilidades.getInstance().getCompras().remove(compra);
                    Utilidades.getInstance().getVendas().remove(venda);
                    return;
                }
            }
        }
        // Um Timer marcado para encerrar a oferta de acordo com o tempo informado
        // pelo comprador
        TimerTask task = new TimerTask() {
            public void run() {
                for(Transacao aux : Utilidades.getInstance().getCompras()){
                    System.out.println("\n" + aux.getId() + ":" + aux.getPreco());
                }
                Utilidades.getInstance().getCompras().remove(compra);
            }
        };
        Timer timer = new Timer("Timer");
        timer.schedule(task, compra.getDelay());
    }

    @PostMapping("venda")
    public void venda(@RequestBody Transacao venda) {
        Optional<Cliente> vendedor = Utilidades.getInstance().getClientes().stream()
                .filter(x -> x.getId() == venda.getIdCliente()).findFirst();
        if(vendedor.isPresent()) {
            Optional<Acao> acao = vendedor.get().getCarteira().stream().filter(x -> x.getId() == venda.getId()).findFirst();
            // Na oferta de venda, é necessário mais verificações.
            // Verificamos se o cliente possui a ação e a quantidade que ele quer vender.
            if(acao.isPresent()) {
                if(acao.get().getQtd() >= venda.getQtd()) {
                    Utilidades.getInstance().getCompras().add(venda);
                    for(Transacao compra : Utilidades.getInstance().getCompras()) {
                        // Com a lista de compras, realiza as verificações necessárias para
                        // ver se existe um par igual
                        if(compra.getIdCliente() != venda.getIdCliente() &&
                                compra.getPreco() == venda.getPreco() &&
                                compra.getId() == venda.getId() &&
                                compra.getQtd() == venda.getQtd()) {
                            // Se existir, realiza a compra/venda para o par
                            // e adiciona a ação na lista de cotações do comprador.
                            // Também dispara uma notificação aos dois envolvidos do par para
                            // sinalizar a conclusão do processo.
                            Optional<Cliente> comprador = Utilidades.getInstance().getClientes().stream()
                                    .filter(x -> x.getId() == compra.getIdCliente()).findFirst();
                            if(comprador.isPresent()) {
                                comprador.get().addAcao(compra.getId(), compra.getQtd());
                                vendedor.get().removeAcao(compra.getId(), compra.getQtd());
                                this.simpMessagingTemplate.convertAndSendToUser(String.valueOf(vendedor.get().getId()), "/notifica","Venda da ação " + venda.getId() + " quantidade: " + venda.getQtd() + " Preço: " + venda.getPreco() + " efetuada com sucesso!");
                                this.simpMessagingTemplate.convertAndSendToUser(String.valueOf(comprador.get().getId()), "/notifica", "Compra da ação " + compra.getId() + " quantidade: " + compra.getQtd() + " Preço: " + compra.getPreco() + " efetuada com sucesso!");
                                comprador.get().getCotacoes().add(compra.getId()); // adicionando a compra na lista de cotações do cliente
                                Utilidades.getInstance().getCompras().remove(compra);
                                Utilidades.getInstance().getVendas().remove(venda);
                            }
                        }
                    }
                    TimerTask task = new TimerTask() {
                        public void run() {
                            System.out.println("entrou no run de venda\n");
                            for(Transacao aux : Utilidades.getInstance().getVendas()){
                                System.out.println("\n" + aux.getId() + ":" + aux.getPreco());
                            }
                            Utilidades.getInstance().getVendas().remove(venda);
                        }
                    };
                    Timer timer = new Timer("Timer");
                    timer.schedule(task, venda.getDelay());
                }
            }
        }
    }

}
