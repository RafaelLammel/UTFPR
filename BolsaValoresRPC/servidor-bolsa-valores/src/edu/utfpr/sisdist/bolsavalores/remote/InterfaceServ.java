package edu.utfpr.sisdist.bolsavalores.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServ extends Remote {

    void registrarInteresse(int id, InterfaceCli interfaceCli) throws RemoteException;

}