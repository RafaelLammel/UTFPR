package edu.utfpr.sisdist.bolsavalores;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Scanner;

import edu.utfpr.sisdist.bolsavalores.impl.CliImpl;
import edu.utfpr.sisdist.bolsavalores.model.Transacao;
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
            System.out.println("1 - Registrar ação na lista de cotações");
            System.out.println("2 - Remover ação da lista de cotações");
            System.out.println("3 - Listar lista de cotações");
            System.out.println("4 - Listar ações na carteira");
            System.out.println("5 - Colocar uma ação para vender");
            System.out.println("6 - Fazer um pedido de compra");
            System.out.println("7 - Adicionar ação na lista de interesse");
            System.out.println("8 - remover ação na lista de interesse");
            System.out.println("9 - Listar lista de interesse");
            opcao = in.nextLine();
            int id;
            float valor;
            int qtd;
            long prazo;
            switch(opcao) {
                case "1":
                    System.out.println("Digite um código de ação: ");
                    id = Integer.parseInt(in.nextLine());
                    referenciaServidor.registrarCotacao(id, referenciaCliente);
                    break;
                case "2":
                    System.out.println("Digite o código que quer eliminar: ");
                    id = Integer.parseInt(in.nextLine());
                    referenciaServidor.removeCotacao(id, referenciaCliente);
                    break;
                case "3":
                    System.out.println("\n========AÇÕES NA COTAÇÃO========");
                    Map<Integer, Float> aux = referenciaServidor.listaCotacao(referenciaCliente);
                    for(Integer chave : aux.keySet()){
                        System.out.println("ID: " + chave + "\n" + "Valor: " + aux.get(chave));
                    }
                    break;
                case "4":
                    System.out.println("\n========AÇÕES NA CARTEIRA========");
                    referenciaServidor.getCarteira(referenciaCliente).forEach(x -> {
                        System.out.println(x.getId() + " ---- " + x.getQtd());
                    });
                    System.out.println();
                    break;
                case "5":
                    System.out.println("digite o código da ação:");
                    id = Integer.parseInt(in.nextLine());
                    System.out.println("digite o valor mínimo da venda:");
                    valor = Float.parseFloat(in.nextLine());
                    System.out.println("digite a quantidade para a venda:");
                    qtd = Integer.parseInt(in.nextLine());
                    System.out.println("digite o prazo da oferta:");
                    prazo = Long.parseLong(in.nextLine());
                    referenciaServidor.venda(new Transacao(id, valor, qtd, prazo, referenciaCliente));
                    break;
                case "6":
                    System.out.println("digite o código da ação:");
                    id = Integer.parseInt(in.nextLine());
                    System.out.println("Digite o valor máximo da compra:");
                    valor = Float.parseFloat(in.nextLine());
                    System.out.println("digite a quantidade para a venda:");
                    qtd = Integer.parseInt(in.nextLine());
                    System.out.println("digite o prazo da oferta:");
                    prazo = Long.parseLong(in.nextLine());
                    referenciaServidor.compra(new Transacao(id, valor, qtd, prazo, referenciaCliente));
                    break;
            }

        } while(opcao != "-1");

        in.close();
    }

}