package edu.utfpr.sisdist.bolsavalores.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import edu.utfpr.sisdist.bolsavalores.model.Acao;
import edu.utfpr.sisdist.bolsavalores.model.Interesse;
import edu.utfpr.sisdist.bolsavalores.model.Transacao;

public interface InterfaceServ extends Remote {

    void adicionaCliente(InterfaceCli interfaceCli) throws RemoteException;

    String registrarCotacao(int id, InterfaceCli interfaceCli) throws RemoteException;

    String removeCotacao(int id, InterfaceCli interfaceCli) throws RemoteException;
    
    Map<Integer, Float> listaCotacao(InterfaceCli interfaceCli) throws RemoteException;

    List<Acao> getCarteira(InterfaceCli interfaceCli) throws RemoteException;

    void compra(Transacao compra) throws RemoteException;

    String venda(Transacao venda) throws RemoteException;

    void registraInteresse(int id, float teto, float piso, InterfaceCli interfaceCli) throws RemoteException;

    List<Interesse> listaInteresses(InterfaceCli interfaceCli) throws RemoteException;

}