package edu.utfpr.sisdist.bolsavalores.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import edu.utfpr.sisdist.bolsavalores.remote.InterfaceCli;
public class CliImpl extends UnicastRemoteObject implements InterfaceCli {

    public CliImpl() throws RemoteException {
    }

    //função que será chamada pelo servidor para notificar o cliente
    @Override
    public void notificarEventos(String msg) throws RemoteException {
        System.out.println(msg);
    }
    
}