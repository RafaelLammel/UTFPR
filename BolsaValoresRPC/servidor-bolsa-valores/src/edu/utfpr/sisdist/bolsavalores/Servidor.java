package edu.utfpr.sisdist.bolsavalores;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import edu.utfpr.sisdist.bolsavalores.impl.ServImpl;
import edu.utfpr.sisdist.bolsavalores.remote.InterfaceServ;

public class Servidor {

    public static void main(String[] args) throws RemoteException {
        System.out.println("Servidor Iniciando...");
        Registry referenciaServicoNomes = LocateRegistry.createRegistry(1099);
        InterfaceServ referenciaServidor = new ServImpl();
        referenciaServicoNomes.rebind("BolsaValores", referenciaServidor);
        System.out.println("Servidor Iniciado!");
    }
    
}