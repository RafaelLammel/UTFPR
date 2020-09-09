package edu.utfpr.sisdist.bolsavalores.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import edu.utfpr.sisdist.bolsavalores.model.Acao;
import edu.utfpr.sisdist.bolsavalores.model.Transacao;

public interface InterfaceServ extends Remote {

    void adicionaCliente(InterfaceCli interfaceCli) throws RemoteException;

    void registrarInteresse(int id, InterfaceCli interfaceCli) throws RemoteException;

    public List<Acao> getCarteira(InterfaceCli interfaceCli) throws RemoteException;

    void compra(Transacao compra) throws RemoteException;

    void venda(Transacao venda) throws RemoteException;

}