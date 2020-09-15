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
        InterfaceCli referenciaCliente = new CliImpl();
        
        referenciaServidor.adicionaCliente(referenciaCliente);

        System.out.println("Bem vindo a bolsa de valores!");

        // Inicialização das variáveis usadas de parâmetro
        String opcao = "";
        Scanner in = new Scanner(System.in);
        int id;
        float valor;
        int qtd;
        long prazo;
        float teto;
        float piso;
        String msg = "";
        do {
            System.out.println("\n\nSelcione uma opção:");
            System.out.println("1 - Registrar ação na lista de cotações");
            System.out.println("2 - Remover ação da lista de cotações");
            System.out.println("3 - Listar lista de cotações");
            System.out.println("4 - Listar ações na carteira");
            System.out.println("5 - Colocar uma ação para vender");
            System.out.println("6 - Fazer um pedido de compra");
            System.out.println("7 - Adicionar ação na lista de interesse");
            System.out.println("8 - Listar lista de interesse");
            System.out.print("Digite a opção: ");
            opcao = in.nextLine();
            switch(opcao) {
                // Registra ação na lista de cotações do cliente
                case "1":
                    System.out.print("\nDigite um código de ação: ");
                    id = Integer.parseInt(in.nextLine());
                    msg = referenciaServidor.registrarCotacao(id, referenciaCliente);
                    if (!msg.isEmpty()) {
                        System.out.println(msg);
                    }
                    break;
                // Remove ação da lista de cotações do cliente
                case "2":
                    System.out.print("\nDigite o código que quer eliminar: ");
                    id = Integer.parseInt(in.nextLine());
                    msg =referenciaServidor.removeCotacao(id, referenciaCliente);
                    if (!msg.isEmpty()) {
                        System.out.println(msg);
                    }
                    break;
                // Lista as cotações na lista de cotações do cliente
                case "3":
                    System.out.println("\n========AÇÕES NA COTAÇÃO========");
                    Map<Integer, Float> aux = referenciaServidor.listaCotacao(referenciaCliente);
                    for(Integer chave : aux.keySet()){
                        System.out.println("ID: " + chave + "\n" + "Valor: " + aux.get(chave));
                    }
                    break;
                // Lista as ações na carteira do cliente
                case "4":
                    System.out.println("\n========AÇÕES NA CARTEIRA========");
                    referenciaServidor.getCarteira(referenciaCliente).forEach(x -> {
                        System.out.println(x.getId() + " ---- " + x.getQtd());
                    });
                    break;
                // Lança uma oferta de venda
                case "5":
                    System.out.print("\nDigite o código da ação: ");
                    id = Integer.parseInt(in.nextLine());
                    System.out.print("Digite o valor mínimo da venda: ");
                    valor = Float.parseFloat(in.nextLine());
                    System.out.print("Digite a quantidade para a venda: ");
                    qtd = Integer.parseInt(in.nextLine());
                    System.out.print("Digite o prazo da oferta: ");
                    prazo = Long.parseLong(in.nextLine());
                    msg = referenciaServidor.venda(new Transacao(id, valor, qtd, prazo, referenciaCliente));
                    if(!msg.isEmpty())
                        System.out.println(msg);
                    break;
                // Lança uma oferta de compra
                case "6":
                    System.out.print("\nDigite o código da ação: ");
                    id = Integer.parseInt(in.nextLine());
                    System.out.print("Digite o valor máximo da compra: ");
                    valor = Float.parseFloat(in.nextLine());
                    System.out.print("Digite a quantidade para a venda: ");
                    qtd = Integer.parseInt(in.nextLine());
                    System.out.print("Digite o prazo da oferta: ");
                    prazo = Long.parseLong(in.nextLine());
                    referenciaServidor.compra(new Transacao(id, valor, qtd, prazo, referenciaCliente));
                    break;
                // Coloca uma ação na lista de interesse para ser notificado quando bater
                // o teto ou o piso
                case "7":
                    System.out.print("\nDigite o código da ação: ");
                    id = Integer.parseInt(in.nextLine());
                    System.out.print("Digite o valor de teto: ");
                    teto = Float.parseFloat(in.nextLine());
                    System.out.print("Digite o valor de piso: ");
                    piso = Float.parseFloat(in.nextLine());
                    referenciaServidor.registraInteresse(id, teto, piso, referenciaCliente);
                case "8":
                    System.out.println("\n========LISTA DE INTERESSES========");
                    referenciaServidor.listaInteresses(referenciaCliente).forEach(x -> {
                        System.out.println(x.getId() + " -- " + x.getTeto() + " -- " + x.getPiso());
                    });
            }

        } while(opcao != "-1");

        in.close();
    }

}