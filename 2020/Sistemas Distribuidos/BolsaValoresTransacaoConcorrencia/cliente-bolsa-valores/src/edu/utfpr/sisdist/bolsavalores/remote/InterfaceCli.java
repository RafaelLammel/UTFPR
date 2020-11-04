package edu.utfpr.sisdist.bolsavalores.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCli extends Remote {

    void notificarEventos(String msg) throws RemoteException;
    
}