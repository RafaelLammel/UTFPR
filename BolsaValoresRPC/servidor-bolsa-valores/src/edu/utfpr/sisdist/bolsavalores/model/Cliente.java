package edu.utfpr.sisdist.bolsavalores.model;

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


}
