package edu.utfpr.sisdist.bolsavalores.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.utfpr.sisdist.bolsavalores.model.Acao;
import edu.utfpr.sisdist.bolsavalores.model.Interesse;
import edu.utfpr.sisdist.bolsavalores.remote.InterfaceCli;
import edu.utfpr.sisdist.bolsavalores.remote.InterfaceServ;

public class ServImpl extends UnicastRemoteObject implements InterfaceServ {

    private List<Interesse> interesses;
    private List<Acao> cotacoes;

    public ServImpl() throws RemoteException {
        interesses = new ArrayList<>();
        cotacoes = new ArrayList<>();
        cotacoes.add(new Acao("Google", 1000));
        cotacoes.add(new Acao("Microsoft", 5000));
    }

    @Override
    public void registrarInteresse(int id, InterfaceCli interfaceCli) throws RemoteException {
        // Verifica se esse cliente já não registrou interesse
        Optional<Interesse> interesse = interesses.stream()
            .filter(x -> x.getIdAcao() == id && x.getInterfaceCli().equals(interfaceCli))
            .findFirst();
        
        // Se não adiciona na lista
        if(!interesse.isPresent())
            interesses.add(new Interesse(id, interfaceCli));
        
        System.out.println("Interesse registrado!");
    }

    @Override
    public List<Acao> listarCotacoes() {
        return cotacoes;
    }
    
}