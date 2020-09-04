package edu.utfpr.sisdist.bolsavalores.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import edu.utfpr.sisdist.bolsavalores.model.Acao;

public interface InterfaceServ extends Remote {

    void adicionaCliente(InterfaceCli interfaceCli) throws RemoteException;

    void registrarInteresse(int id, InterfaceCli interfaceCli) throws RemoteException;
    
    List<Acao> listarCotacoes() throws RemoteException;

}