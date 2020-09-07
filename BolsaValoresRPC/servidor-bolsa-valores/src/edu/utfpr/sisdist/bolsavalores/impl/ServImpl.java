package edu.utfpr.sisdist.bolsavalores.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.utfpr.sisdist.bolsavalores.model.Acao;
import edu.utfpr.sisdist.bolsavalores.model.Cliente;
import edu.utfpr.sisdist.bolsavalores.model.Transacao;
import edu.utfpr.sisdist.bolsavalores.remote.InterfaceCli;
import edu.utfpr.sisdist.bolsavalores.remote.InterfaceServ;

public class ServImpl extends UnicastRemoteObject implements InterfaceServ {

    private List<Acao> cotacoes;
    private List<Cliente> clientes;
    private List<Transacao> compras;
    private List<Transacao> vendas;

    public ServImpl() throws RemoteException {
        cotacoes = new ArrayList<>();
        clientes = new ArrayList<>();
        compras = new ArrayList<>();
        vendas = new ArrayList<>();
        cotacoes.add(new Acao(1, "Google", 1000));
        cotacoes.add(new Acao(2, "Microsoft", 5000));
    }

    @Override
    public void adicionaCliente(InterfaceCli interfaceCli) throws RemoteException{
        clientes.add(new Cliente(interfaceCli));
    }

    @Override
    public void registrarInteresse(int id, InterfaceCli interfaceCli) throws RemoteException {
        Optional<Acao> acao = cotacoes.stream()
            .filter(x -> x.getId() == id).findFirst();
        
        if(acao.isPresent()) {
            Optional<Cliente> cliente = clientes.stream()
                .filter(x -> x.getinterfaceCli().equals(interfaceCli)).findFirst();
            if(cliente.isPresent()) {
                cliente.get().getInteresses().add(id);
            }
            else {
                System.out.println("Cliente não encontrado!");
            }
        }
        else {
            System.out.println("Ação não está presente na lista de cotações!");
        }
    }

    @Override
    public List<Acao> listarCotacoes() {
        return cotacoes;
    }

    @Override
    public void compra(Transacao compra){
        compras.add(compra);
        verificaTransacoes();
    }

    @Override
    public void venda(Transacao venda){
        vendas.add(venda);
        verificaTransacoes();
    }

    public void verificaTransacoes(){
        for(Transacao compra : compras){
            for(Transacao venda : vendas){
                if(compra.getId() == venda.getId()){
                    if(compra.getQtd() > venda.getQtd()){
                        Optional<Cliente> cliente = clientes.stream()
                            .filter(x -> x.getinterfaceCli().equals(compra.getReferenciaCliente())).findFirst();
                        if(cliente.isPresent()){
                            cliente.carteira.add()
                        }
                    }
                }
               
        }
    }

    
}