package edu.utfpr.sisdist.bolsavalores.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.utfpr.sisdist.bolsavalores.remote.InterfaceCli;

public class Cliente {

    private InterfaceCli interfaceCli;
    private List<Acao> carteira;
    private List<Integer> cotacoes;
    private List<Interesse> interesses;

    public Cliente(InterfaceCli interfaceCli) {
        this.interfaceCli = interfaceCli;
        this.carteira = new ArrayList<>();
        this.cotacoes = new ArrayList<>();
        this.interesses = new ArrayList<>();
    }
    
    public InterfaceCli getInterfaceCli() {
        return this.interfaceCli;
    }

    public List<Acao> getAcoes() {
        return this.carteira;
    }

    public List<Integer> getCotacoes() {
        return this.cotacoes;
    }

    public void registrarCotacao(int id) {
        this.cotacoes.add(id);
    }

    public List<Acao> getCarteira() {
        return this.carteira;
    }

    public List<Interesse> getInteresses() {
        return this.interesses;
    }

    public void addAcao(int id, int qtd) {
        Optional<Acao> acao = this.carteira.stream().filter(x -> x.getId() == id).findFirst();
        if(acao.isPresent()) {
            acao.get().setQtd(qtd + acao.get().getQtd());
        }
        else {
            carteira.add(new Acao(id, qtd));
        }
    }

    public void removeAcao(int id, int qtd) {
        Optional<Acao> acao = this.carteira.stream().filter(x -> x.getId() == id).findFirst();
        int novaQtd = acao.get().getQtd() - qtd;
        if(novaQtd == 0) {
            carteira.remove(acao.get());
        }
        else {
            acao.get().setQtd(novaQtd);
        }
    }

    /**
     * statusCompraVenda = 0 -> compra
     * statusCompraVenda = 1 -> venda
     */
    public boolean preparar(Transacao compraVenda, int statusCompraVenda) {
        try {
            File myObj = new File("CarteiraIntermediaria_" + interfaceCli.hashCode() + ".txt");
            myObj.createNewFile();
            FileWriter fileWriter = new FileWriter("CarteiraIntermediaria_" + interfaceCli.hashCode() + ".txt");
            int idRetirar = compraVenda.getId();
            for(Acao acao : carteira) {         
                if(idRetirar != acao.getId()) {
                    fileWriter.write(acao.toString()+"\n");
                }
                else if((acao.getQtd() - compraVenda.getQtd() > 0) && statusCompraVenda == 1) {
                    fileWriter.write(compraVenda.getId()+";"+(acao.getQtd() - compraVenda.getQtd())+"\n");
                }
            }
            if(statusCompraVenda == 0) {
                Optional<Acao> acao = this.carteira.stream().filter(x -> x.getId() == compraVenda.getId()).findFirst();
                if(acao.isPresent()) {
                    fileWriter.write(compraVenda.getId()+";"+(acao.get().getQtd()+compraVenda.getQtd())+"\n");
                }
                else {
                    fileWriter.write(compraVenda.getId()+";"+compraVenda.getQtd()+"\n");
                }
            }
            fileWriter.close();
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
    }

    public void abortar() {
        //log abortado
        File f = new File("CarteiraIntermediaria_" + interfaceCli.hashCode() + ".txt");
        f.delete();
    }

    public boolean efetuar() {
        List<Acao> carteiraNova = new ArrayList<>();
        try {
            FileReader arq = new FileReader("CarteiraIntermediaria_" + interfaceCli.hashCode() + ".txt");
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            while(linha != null){
                String[] id = linha.split(";");
                carteiraNova.add(new Acao(Integer.parseInt(id[0]), Integer.parseInt(id[1])));
                linha = lerArq.readLine();
            }      
            arq.close();
            File f = new File("CarteiraIntermediaria_" + interfaceCli.hashCode() + ".txt");
            f.delete();
            this.carteira = carteiraNova;
            Optional<Integer> cotacao = this.cotacoes.stream().filter(x -> x == this.carteira.get(this.carteira.size()-1).getId()).findFirst();
            if(!cotacao.isPresent()) {
                this.cotacoes.add(this.carteira.get(this.carteira.size()-1).getId());
            }
            return true;
        }
        catch(Exception e) {
            System.out.println("An error occurred.");
            return false;
        }
    }

}
