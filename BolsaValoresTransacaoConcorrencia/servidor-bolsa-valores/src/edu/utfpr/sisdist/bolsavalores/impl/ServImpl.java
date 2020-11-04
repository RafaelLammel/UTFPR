package edu.utfpr.sisdist.bolsavalores.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import edu.utfpr.sisdist.bolsavalores.model.*;
import edu.utfpr.sisdist.bolsavalores.remote.*;

public class ServImpl extends UnicastRemoteObject implements InterfaceServ {

    private int idAcaoAtual;
    private List<Cliente> clientes;
    private List<Transacao> compras;
    private List<Transacao> vendas;
    Map<Integer, Float> acoes = new HashMap<Integer, Float>();
    Random gerador = new Random();

    /**
     * Ao iniciar o servidor, iniciamos junto todos os seus atributos e um timer que
     * atualiza aleatóriamente o valor das ações
     */
    public ServImpl() throws RemoteException {
        clientes = new ArrayList<>();
        compras = new ArrayList<>();
        vendas = new ArrayList<>();
        idAcaoAtual = 1;
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    atualizaValor();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer("Timer");
        timer.schedule(task, 30000, 30000);
    }

    /**
     * Quando um cliente se conecta, essa função cria um novo cliente e guarda os dados dele
     * (optamos por cada cliente começar com 2 ações)
     * E t
     */
    @Override
    public void adicionaCliente(InterfaceCli interfaceCli) throws RemoteException{
        Cliente cliente = new Cliente(interfaceCli);
        cliente.addAcao(idAcaoAtual, 50);
        cliente.addAcao(idAcaoAtual+1, 50);
        cliente.getCotacoes().add(idAcaoAtual);
        cliente.getCotacoes().add(idAcaoAtual+1);
        idAcaoAtual += 2;
        clientes.add(cliente);
        for (Acao acao : cliente.getCarteira()){
            acoes.put(acao.getId(), (float)gerador.nextInt(100));
        }
    }

    /**
     * Retorna as ações na carteira do cliente
     */
    @Override
    public List<Acao> getCarteira(InterfaceCli interfaceCli) throws RemoteException {
        Optional<Cliente> cliente = this.clientes.stream().filter(x -> x.getInterfaceCli().equals(interfaceCli)).findFirst();
        if(cliente.isPresent()) {
            return cliente.get().getCarteira();
        }
        return null;
    }

    /**
     * Registra uma cotação na lista de cotações do cliente
     */
    @Override
    public String registrarCotacao(int id, InterfaceCli interfaceCli) throws RemoteException {
        Optional<Cliente> cliente = clientes.stream()
            .filter(x -> x.getInterfaceCli().equals(interfaceCli)).findFirst();
        if(cliente.isPresent()) {
            for(Integer acao : acoes.keySet()) {
                if(acao == id) {
                    cliente.get().registrarCotacao(id);
                    return "";
                }
            }
            return "Ação não existe na bolsa!";
        }
        return "Cliente não encontrado!";
    }

    /**
     * Remove uma ação da lista de cotações do cliente
     */
    @Override
    public String removeCotacao(int id, InterfaceCli interfaceCli) throws RemoteException {
        Optional<Cliente> cliente = this.clientes.stream()
            .filter(x -> x.getInterfaceCli().equals(interfaceCli)).findFirst();
        if(cliente.isPresent()) {
            if(cliente.get().getCotacoes().remove(Integer.valueOf(id))) {
                return "";
            }
            return "Ação não está na sua lista de cotações!";
        }
        return "Cliente não encontrado!";
    }

    @Override
    public Map<Integer, Float> listaCotacao(InterfaceCli interfaceCli) throws RemoteException {
        Optional<Cliente> cliente = this.clientes.stream()
            .filter(x -> x.getInterfaceCli().equals(interfaceCli)).findFirst();
        if(cliente.isPresent()) {
            Map<Integer, Float> c = new HashMap<Integer, Float>();
            for(Integer acao : cliente.get().getCotacoes()) {
                c.put(acao, acoes.get(acao));
            }
            return c;
        }
        System.out.println("Cliente não encontrado!");
        return null;
    }

    /**
     * Guarda a oferta de compra em uma lista de ofertas de compra até que o prazo expire
     */
    @Override
    public void compra(Transacao compra) throws RemoteException{
        this.compras.add(compra);
        synchronized (compra) {
            for(Transacao venda : this.vendas) {
                // Com a lista de vendas, realiza as verificações necessárias para
                // ver se existe um par igual
                synchronized (venda) {
                    if(compra.getReferenciaCliente() != venda.getReferenciaCliente() &&
                        compra.getPreco() == venda.getPreco() &&
                        compra.getId() == venda.getId() &&
                        compra.getQtd() == venda.getQtd()) {
                            Optional<Cliente> comprador = this.clientes.stream().filter(x -> x.getInterfaceCli().equals(compra.getReferenciaCliente())).findFirst();
                            Optional<Cliente> vendedor = this.clientes.stream().filter(x -> x.getInterfaceCli().equals(venda.getReferenciaCliente())).findFirst();
                            // Se existir, realiza a compra/venda para o par
                            // e adiciona a ação na lista de cotações do comprador.
                            // Também dispara uma notificação aos dois envolvidos do par para
                            // sinalizar a conclusão do processo.
                            if(comprador.isPresent() && vendedor.isPresent()) {
                                Coordenador coordenador = new Coordenador();
                                if(coordenador.abrirTransacao(compra, venda, comprador.get(), vendedor.get())) {
                                    this.compras.remove(compra);
                                    this.vendas.remove(venda);
                                    comprador.get().getInterfaceCli().notificarEventos("\nCompra da ação " + compra.getId() + " quantidade: " + compra.getQtd() + " Preço: " + compra.getPreco() + " efetuada com sucesso!\n");
                                    vendedor.get().getInterfaceCli().notificarEventos("\nVenda da ação " + venda.getId() + " quantidade: " + venda.getQtd() + " Preço: " + venda.getPreco() + " efetuada com sucesso!\n");
                                }
                                return;
                            }
                        }
                }
            }
        }
        // Um Timer marcado para encerrar a oferta de acordo com o tempo informado
        // pelo comprador
        TimerTask task = new TimerTask() {
            public void run() {
                for(Transacao aux : compras){
                    System.out.println("\n" + aux.getId() + ":" + aux.getPreco());
                }
                compras.remove(compra);
            }
        };
        Timer timer = new Timer("Timer");
        timer.schedule(task, compra.getDelay());
    }

    /**
     * Guarda a oferta de venda em uma lista de ofertas de venda até que o prazo expire
     */
    @Override
    public String venda(Transacao venda) throws RemoteException {
        Optional<Cliente> vendedor = this.clientes.stream().filter(x -> x.getInterfaceCli().equals(venda.getReferenciaCliente())).findFirst();
        if(vendedor.isPresent()) {
            Optional<Acao> acao = vendedor.get().getCarteira().stream().filter(x -> x.getId() == venda.getId()).findFirst();
            // Na oferta de venda, é necessário mais verificações.
            // Verificamos se o cliente possui a ação e a quantidade que ele quer vender.
            if(acao.isPresent()) {
                if(acao.get().getQtd() >= venda.getQtd()) { 
                    this.vendas.add(venda);
                    synchronized(venda) {
                        for(Transacao compra : this.compras) {
                            synchronized(compra) {
                                // Com a lista de compras, realiza as verificações necessárias para
                                // ver se existe um par igual
                                if(compra.getReferenciaCliente() != venda.getReferenciaCliente() &&
                                   compra.getPreco() == venda.getPreco() &&
                                   compra.getId() == venda.getId() &&
                                   compra.getQtd() == venda.getQtd()) {
                                        // Se existir, realiza a compra/venda para o par
                                        // e adiciona a ação na lista de cotações do comprador.
                                        // Também dispara uma notificação aos dois envolvidos do par para
                                        // sinalizar a conclusão do processo.
                                        Optional<Cliente> comprador = this.clientes.stream().filter(x -> x.getInterfaceCli().equals(compra.getReferenciaCliente())).findFirst();
                                        if(comprador.isPresent()) {
                                            Coordenador coordenador = new Coordenador();
                                            if(coordenador.abrirTransacao(compra, venda, comprador.get(), vendedor.get())) {
                                                this.compras.remove(compra);
                                                this.vendas.remove(venda);
                                                comprador.get().getInterfaceCli().notificarEventos("\nCompra da ação " + compra.getId() + " quantidade: " + compra.getQtd() + " Preço: " + compra.getPreco() + " efetuada com sucesso!\n");
                                                vendedor.get().getInterfaceCli().notificarEventos("\nVenda da ação " + venda.getId() + " quantidade: " + venda.getQtd() + " Preço: " + venda.getPreco() + " efetuada com sucesso!\n");
                                            }
                                            return "";
                                        }
                                }
                            }
                        }
                    }
                    TimerTask task = new TimerTask() {
                        public void run() {
                            System.out.println("entrou no run de venda\n");
                            for(Transacao aux : vendas){
                                System.out.println("\n" + aux.getId() + ":" + aux.getPreco());
                            }
                            vendas.remove(venda);
                        }
                    };
                    Timer timer = new Timer("Timer");
                    timer.schedule(task, venda.getDelay());
                    return "";
                }
                return "Você tem menos da quantidade que colocou na venda!";
            }
            return "Você não possui essa ação!";
        }
        return "Cliente não encontrado!";
    }

    /**
     * Registra uma açãi na lista de interesses
     */
    @Override
    public void registraInteresse(int id, float teto, float piso, InterfaceCli interfaceCli) throws RemoteException {
        Interesse interesse = new Interesse(id, teto, piso);
        Optional<Cliente> cliente = this.clientes.stream().filter(x -> x.getInterfaceCli().equals(interfaceCli)).findFirst();
        if(cliente.isPresent()) {
            cliente.get().getInteresses().add(interesse);
            notificaMargem();
        }
        else {
            System.out.println("Cliente não encontrado");
        }
    }
    
    /**
     * Adiciona ou decrementa um valor aleatório de todas as ações
     */
    public void atualizaValor() throws RemoteException{
        int max = 50;
        int min = -50;
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
    public void notificaMargem() throws RemoteException{
        for(Cliente cliente : this.clientes) {
            if(cliente.getInteresses().size() > 0) {
                for(Interesse interesse : cliente.getInteresses()) {
                    float valor = acoes.get(interesse.getId());
                    //atualiza "ultimo valor", para que o cliente não receba notificações repetidas
                    if (interesse.getUltimoValor() != valor) {
                        if(interesse.getPiso() > valor) {
                            cliente.getInterfaceCli().notificarEventos("\nA ação de Id " + interesse.getId() + " ficou abaixo do seu limite de piso: \n" +
                                                "Limite definido: " + interesse.getPiso() +
                                                " Valor atual: " + valor);
                        }
                        if(interesse.getTeto() < valor) {
                            cliente.getInterfaceCli().notificarEventos("\nA ação de Id " + interesse.getId() + " ficou acima do seu limite de teto: \n" +
                                                "Limite definido: " + interesse.getTeto() +
                                                " Valor atual: " + valor);
                        }
                        interesse.setUltimoValor(valor);

                    }
                }
            }
        }
    }
    
    /**
     * Lista os interesses
     */                    
    @Override
    public List<Interesse> listaInteresses(InterfaceCli interfaceCli) throws RemoteException{
        Optional<Cliente> cliente = this.clientes.stream().filter(x -> x.getInterfaceCli().equals(interfaceCli)).findFirst();
        return cliente.get().getInteresses();
    }

}