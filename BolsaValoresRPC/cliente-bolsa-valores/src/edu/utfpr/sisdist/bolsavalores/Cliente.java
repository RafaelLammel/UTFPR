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

        referenciaServidor.adicionaCliente(referenciaCliente);

        System.out.println("Bem vindo a bolsa de valores!");

        String opcao = "";
        Scanner in = new Scanner(System.in);
        do {
            System.out.println("Selcione uma opção:");
            System.out.println("1 - Registrar interesse em uma ação");
            System.out.println("2 - Obter lista de cotações corrente");
            System.out.println("3 - Colocar uma ação para vender");
            System.out.println("4 - Fazer um pedido de compra");
            opcao = in.nextLine();
            switch(opcao) {
                case "1":
                    System.out.println("digite um código de ação");
                    int id = Integer.parseInt(in.nextLine());
                    referenciaServidor.registrarInteresse(id, referenciaCliente);
                    break;
                case "2":
                    System.out.println("\n========AÇÕES NA CARTEIRA========");
                    referenciaServidor.getCarteira(referenciaCliente).forEach(x -> {
                        System.out.println(x.getId() + " ---- " + x.getQtd());
                    });
                    System.out.println();
                    break;
                case "3":
                    //System.out.println("digite o código da ação para ser vendida:");
                    //int id = Integer.parseInt(in.nextLine());
                    
                    break;
                case "4":
                    /*System.out.println("digite o código da ação:");
                    int id = Integer.parseInt(in.nextLine());
                    System.out.println("Digite o valor:");
                    float valor = Float.parseFloat(in.nextLine());*/
                    break;
            }

        } while(opcao != "-1");

        in.close();
    }

}