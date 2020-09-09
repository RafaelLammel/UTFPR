package edu.utfpr.sisdist.bolsavalores.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

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
        Optional<Cliente> cliente = this.clientes.stream().filter(x -> x.getinterfaceCli().equals(interfaceCli)).findFirst();
        if(cliente.isPresent()) {
            return cliente.get().getCarteira();
        }
        return null;
    }

    @Override
    public void registrarCotacao(int id, InterfaceCli interfaceCli) throws RemoteException {
        Optional<Cliente> cliente = clientes.stream()
            .filter(x -> x.getinterfaceCli().equals(interfaceCli)).findFirst();
        if(cliente.isPresent()) {
            cliente.get().registrarCotacao(id);
        }
        else {
            System.out.println("Cliente não encontrado!");
        }
    }

    @Override
    public void removeCotacao(int id, InterfaceCli interfaceCli) throws RemoteException {
        Optional<Cliente> cliente = this.clientes.stream()
            .filter(x -> x.getinterfaceCli().equals(interfaceCli)).findFirst();
        if(cliente.isPresent()) {
            cliente.get().getCotacoes().remove(Integer.valueOf(id));
        }
        else {
            System.out.println("Cliente não encontrado!");
        }
    }

    @Override
    public Map<Integer, Float> listaCotacao(InterfaceCli interfaceCli) throws RemoteException {
        Optional<Cliente> cliente = this.clientes.stream()
            .filter(x -> x.getinterfaceCli().equals(interfaceCli)).findFirst();
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
    public void compra(Transacao compra) throws RemoteException{
        compras.add(compra);
        for(Transacao venda : this.vendas) {
            if(compra.getReferenciaCliente() != venda.getReferenciaCliente() &&
                compra.getPreco() == venda.getPreco() &&
                compra.getId() == venda.getId() &&
                compra.getQtd() == venda.getQtd()) {
                    Optional<Cliente> comprador = this.clientes.stream().filter(x -> x.getinterfaceCli().equals(compra.getReferenciaCliente())).findFirst();
                    Optional<Cliente> vendedor = this.clientes.stream().filter(x -> x.getinterfaceCli().equals(venda.getReferenciaCliente())).findFirst();
                    if(comprador.isPresent() && vendedor.isPresent()) {
                        comprador.get().addAcao(compra.getId(), compra.getQtd());
                        vendedor.get().removeAcao(compra.getId(), compra.getQtd());
                    }
            }
        }
    }

    @Override
    public void venda(Transacao venda) throws RemoteException{
        vendas.add(venda);
        for(Transacao compra : this.compras) {
            if(compra.getReferenciaCliente() != venda.getReferenciaCliente() &&
               compra.getPreco() == venda.getPreco() &&
               compra.getId() == venda.getId() &&
               compra.getQtd() == venda.getQtd()) {
                    Optional<Cliente> comprador = this.clientes.stream().filter(x -> x.getinterfaceCli().equals(compra.getReferenciaCliente())).findFirst();
                    Optional<Cliente> vendedor = this.clientes.stream().filter(x -> x.getinterfaceCli().equals(venda.getReferenciaCliente())).findFirst();
                    if(comprador.isPresent() && vendedor.isPresent()) {
                        comprador.get().addAcao(compra.getId(), compra.getQtd());
                        vendedor.get().removeAcao(compra.getId(), compra.getQtd());
                    }
            }
        }
    }

}