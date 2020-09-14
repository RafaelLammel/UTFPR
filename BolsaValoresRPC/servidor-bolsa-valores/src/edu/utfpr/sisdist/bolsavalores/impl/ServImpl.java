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

    @Override
    public void adicionaCliente(InterfaceCli interfaceCli) throws RemoteException{
        Cliente cliente = new Cliente(interfaceCli);
        cliente.addAcao(idAcaoAtual++, 50);
        cliente.addAcao(idAcaoAtual++, 50);
        clientes.add(cliente);
        for (Acao acao : cliente.getCarteira()){
            acoes.put(acao.getId(), (float)gerador.nextInt(100));
        }
    }

    @Override
    public List<Acao> getCarteira(InterfaceCli interfaceCli) throws RemoteException {
        Optional<Cliente> cliente = this.clientes.stream().filter(x -> x.getInterfaceCli().equals(interfaceCli)).findFirst();
        if(cliente.isPresent()) {
            return cliente.get().getCarteira();
        }
        return null;
    }

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

    @Override
    public synchronized void compra(Transacao compra) throws RemoteException{
        this.compras.add(compra);
        for(Transacao venda : this.vendas) {
            if(compra.getReferenciaCliente() != venda.getReferenciaCliente() &&
                compra.getPreco() == venda.getPreco() &&
                compra.getId() == venda.getId() &&
                compra.getQtd() == venda.getQtd()) {
                    Optional<Cliente> comprador = this.clientes.stream().filter(x -> x.getInterfaceCli().equals(compra.getReferenciaCliente())).findFirst();
                    Optional<Cliente> vendedor = this.clientes.stream().filter(x -> x.getInterfaceCli().equals(venda.getReferenciaCliente())).findFirst();
                    if(comprador.isPresent() && vendedor.isPresent()) {
                        comprador.get().addAcao(compra.getId(), compra.getQtd());
                        vendedor.get().removeAcao(compra.getId(), compra.getQtd());
                        comprador.get().getInterfaceCli().notificarEventos("Compra da ação " + compra.getId() + "quantidade: " + compra.getQtd() + "Preço: " + compra.getPreco() + "efetuada com sucesso!" + "\n");
                        vendedor.get().getInterfaceCli().notificarEventos("Venda da ação " + venda.getId() + "quantidade: " + venda.getQtd() + "Preço: " + venda.getPreco() + "efetuada com sucesso!" + "\n");
                        comprador.get().getCotacoes().add(compra.getId()); // adicionando a compra na lista de cotações do cliente
                        this.compras.remove(compra);
                        this.vendas.remove(venda);
                        return;
                    }
            }
        }
        TimerTask task = new TimerTask() {
            public void run() {
                System.out.println("entrou no run de compra\n");
                for(Transacao aux : compras){
                    System.out.println("\n" + aux.getId() + ":" + aux.getPreco());
                }
                compras.remove(compra);
            }
        };
        Timer timer = new Timer("Timer");
        timer.schedule(task, compra.getDelay());
    }

    @Override
    public synchronized String venda(Transacao venda) throws RemoteException {
        Optional<Cliente> vendedor = this.clientes.stream().filter(x -> x.getInterfaceCli().equals(venda.getReferenciaCliente())).findFirst();
        if(vendedor.isPresent()) {
            Optional<Acao> acao = vendedor.get().getCarteira().stream().filter(x -> x.getId() == venda.getId()).findFirst();
            if(acao.isPresent()) {
                if(acao.get().getQtd() >= venda.getQtd()) { 
                    this.vendas.add(venda);
                    for(Transacao compra : this.compras) {
                        if(compra.getReferenciaCliente() != venda.getReferenciaCliente() &&
                           compra.getPreco() == venda.getPreco() &&
                           compra.getId() == venda.getId() &&
                           compra.getQtd() == venda.getQtd()) {
                                Optional<Cliente> comprador = this.clientes.stream().filter(x -> x.getInterfaceCli().equals(compra.getReferenciaCliente())).findFirst();
                                if(comprador.isPresent()) {
                                    comprador.get().addAcao(compra.getId(), compra.getQtd());
                                    vendedor.get().removeAcao(compra.getId(), compra.getQtd());
                                    comprador.get().getInterfaceCli().notificarEventos("Compra da ação " + compra.getId() + "quantidade: " + compra.getQtd() + "Preço: " + compra.getPreco() + "efetuada com sucesso!" + "\n");
                                    vendedor.get().getInterfaceCli().notificarEventos("Venda da ação " + venda.getId() + "quantidade: " + venda.getQtd() + "Preço: " + venda.getPreco() + "efetuada com sucesso!" + "\n");
                                    comprador.get().getCotacoes().add(compra.getId()); // adicionando a compra na lista de cotações do cliente
                                    this.compras.remove(compra);
                                    this.vendas.remove(venda);
                                    return "";
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
    }

    public void notificaMargem() throws RemoteException{
        for(Cliente cliente : this.clientes) {
            if(cliente.getInteresses().size() > 0) {
                for(Interesse interesse : cliente.getInteresses()) {
                    float valor = acoes.get(interesse.getId());
                    if (interesse.getUltimoValor() != valor) {
                        if(interesse.getPiso() > valor) {
                            cliente.getInterfaceCli().notificarEventos("A ação de Id " + interesse.getId() + " caiu abaixo do seu limite de piso: \n" +
                                                "Limite definido: " + interesse.getPiso() +
                                                "Valor atual: " + valor);
                        }
                        if(interesse.getTeto() < valor) {
                            cliente.getInterfaceCli().notificarEventos("A ação de Id " + interesse.getId() + " caiu abaixo do seu limite de piso: \n" +
                                                "Limite definido: " + interesse.getTeto() +
                                                "Valor atual: " + valor);
                        }
                        interesse.setUltimoValor(valor);
                    }
                }
            }
        }
    }

    @Override
    public List<Interesse> listaInteresses(InterfaceCli interfaceCli) throws RemoteException{
        Optional<Cliente> cliente = this.clientes.stream().filter(x -> x.getInterfaceCli().equals(interfaceCli)).findFirst();
        return cliente.get().getInteresses();
    }

}