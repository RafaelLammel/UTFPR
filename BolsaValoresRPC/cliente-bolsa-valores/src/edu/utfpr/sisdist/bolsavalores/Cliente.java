package edu.utfpr.sisdist.bolsavalores;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import edu.utfpr.sisdist.bolsavalores.impl.CliImpl;
import edu.utfpr.sisdist.bolsavalores.remote.InterfaceCli;
import edu.utfpr.sisdist.bolsavalores.remote.InterfaceServ;

public class Cliente {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry referenciaServicoNomes = LocateRegistry.getRegistry();
        InterfaceServ referenciaServidor = (InterfaceServ)referenciaServicoNomes.lookup("BolsaValores");
        InterfaceCli referenciaCliente = new CliImpl(referenciaServidor);

        System.out.println("Bem vindo a bolsa de valores!");
        
        String opcao = "";
        Scanner in = new Scanner(System.in);
        do {
            System.out.println("Selcione uma opção:");
            System.out.println("1 - Registrar interesse em uma ação");
            System.out.println("2 - Obter lista de cotações corrente");
            opcao = in.nextLine();
            switch(opcao) {
                case "1":
                    referenciaServidor.registrarInteresse(1, referenciaCliente);
                    break;
                case "2":
                    System.out.println("\n========LISTA DE COTAÇÕES========");
                    referenciaServidor.listarCotacoes().forEach(x -> {
                        System.out.println(x.getPreco() + " ---- " + x.getNome());
                    });
                    System.out.println();
                    break;
            }

        } while(opcao != "-1");

        in.close();
    }

}