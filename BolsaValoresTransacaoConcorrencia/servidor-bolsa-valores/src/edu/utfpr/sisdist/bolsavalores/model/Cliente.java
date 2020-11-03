package edu.utfpr.sisdist.bolsavalores.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.utfpr.sisdist.bolsavalores.impl.Coordenador;
import edu.utfpr.sisdist.bolsavalores.remote.InterfaceCli;

public class Cliente {

    private InterfaceCli interfaceCli;
    private List<Acao> carteira;
    private List<Integer> cotacoes;
    private List<Interesse> interesses;
    private File log;
    private File carteiraFinal;

    public Cliente(InterfaceCli interfaceCli) {
        this.interfaceCli = interfaceCli;
        this.carteira = new ArrayList<>();
        this.cotacoes = new ArrayList<>();
        this.interesses = new ArrayList<>();
        this.log = new File("Log_Transacoes_"+ interfaceCli.hashCode() + ".txt");
        this.carteiraFinal = new File("CarteiraFinal_" + interfaceCli.hashCode() + ".txt");
        try {
            this.log.createNewFile();
            this.carteiraFinal.createNewFile();
        }
        catch(Exception e){
            
        }
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
        atualizaRegistroCarteira();
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

    public void atualizaRegistroCarteira() {
        try {
            FileWriter fileWriter = new FileWriter("CarteiraFinal_" + interfaceCli.hashCode() + ".txt");
            for(Acao acao : this.carteira) {
                fileWriter.write(acao.getId() + ";" + acao.getQtd() + "\n");
            }
            fileWriter.close();
        }
        catch(Exception e){
            
        }
    }

    /**
     * statusCompraVenda = 0 -> compra
     * statusCompraVenda = 1 -> venda
     */
    public boolean preparar(Transacao compraVenda, int statusCompraVenda, Coordenador coordenador){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        try {
            File myObj = new File("CarteiraIntermediaria_" + interfaceCli.hashCode() + ".txt");
            FileWriter fileWriterLog = new FileWriter("Log_Transacoes_" + this.interfaceCli.hashCode() + ".txt", true);
            fileWriterLog.write(dtf.format(LocalDateTime.now()) + " - " + coordenador.getId() + " - " + coordenador.obterEstadoTransacao() + "\n");
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
            fileWriterLog.close();
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
    }

    public void abortar(Coordenador coordenador) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        //log abortado
        File f = new File("CarteiraIntermediaria_" + interfaceCli.hashCode() + ".txt");
        f.delete();
        try {
            FileWriter fileWriterLog = new FileWriter("Log_Transacoes_" + this.interfaceCli.hashCode() + ".txt", true);
            fileWriterLog.write(dtf.format(LocalDateTime.now()) + " - " + coordenador.getId() + " - " + coordenador.obterEstadoTransacao() + "\n");
            fileWriterLog.close();
        } catch (Exception e) {

        }
    }

    public boolean efetuar(Coordenador coordenador) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
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
            FileWriter fileWriterLog = new FileWriter("Log_Transacoes_" + this.interfaceCli.hashCode() + ".txt", true);
            fileWriterLog.write(dtf.format(LocalDateTime.now()) + " - " + coordenador.getId() + " - " + coordenador.obterEstadoTransacao() + "\n");
            fileWriterLog.close();
            atualizaRegistroCarteira();
            return true;
        }
        catch(Exception e) {
            System.out.println("An error occurred.");
            return false;
        }
    }

    public void registrarLogEfetuado(Coordenador coordenador) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        try {
            FileWriter fileWriterLog = new FileWriter("Log_Transacoes_" + this.interfaceCli.hashCode() + ".txt", true);
            fileWriterLog.write(dtf.format(LocalDateTime.now()) + " - " + coordenador.getId() + " - " + coordenador.obterEstadoTransacao() + "\n");
            fileWriterLog.close();
        }
        catch (Exception e) {

        }
    }

}
