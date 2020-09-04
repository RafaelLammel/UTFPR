package edu.utfpr.sisdist.bolsavalores.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.utfpr.sisdist.bolsavalores.model.Acao;
import edu.utfpr.sisdist.bolsavalores.model.Cliente;
import edu.utfpr.sisdist.bolsavalores.remote.InterfaceCli;
import edu.utfpr.sisdist.bolsavalores.remote.InterfaceServ;

public class ServImpl extends UnicastRemoteObject implements InterfaceServ {

    private List<Acao> cotacoes;
    private List<Cliente> clientes;

    public ServImpl() throws RemoteException {
        cotacoes = new ArrayList<>();
        clientes = new ArrayList<>();
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
    
}