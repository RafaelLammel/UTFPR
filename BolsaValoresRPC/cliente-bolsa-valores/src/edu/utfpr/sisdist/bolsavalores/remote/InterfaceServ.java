package edu.utfpr.sisdist.bolsavalores.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import edu.utfpr.sisdist.bolsavalores.model.Acao;
import edu.utfpr.sisdist.bolsavalores.model.Transacao;

public interface InterfaceServ extends Remote {

    void adicionaCliente(InterfaceCli interfaceCli) throws RemoteException;

    void registrarCotacao(int id, InterfaceCli interfaceCli) throws RemoteException;

    void removeCotacao(int id, InterfaceCli interfaceCli) throws RemoteException;
    
    Map<Integer, Float> listaCotacao(InterfaceCli interfaceCli) throws RemoteException;

    List<Acao> getCarteira(InterfaceCli interfaceCli) throws RemoteException;

    void compra(Transacao compra) throws RemoteException;

    void venda(Transacao venda) throws RemoteException;

}