package edu.utfpr.sisdist.bolsavalores.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import edu.utfpr.sisdist.bolsavalores.remote.InterfaceCli;
import edu.utfpr.sisdist.bolsavalores.remote.InterfaceServ;

public class CliImpl extends UnicastRemoteObject implements InterfaceCli {

    private InterfaceServ interfaceServ;

    public CliImpl(InterfaceServ interfaceServ) throws RemoteException {
        this.interfaceServ = interfaceServ;
    }

    @Override
    public void notificarEventos(String msg) throws RemoteException {
        System.out.println(msg);
    }
    
}