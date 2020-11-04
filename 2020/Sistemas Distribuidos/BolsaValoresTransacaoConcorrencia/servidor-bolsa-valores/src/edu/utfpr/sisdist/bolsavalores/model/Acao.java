package edu.utfpr.sisdist.bolsavalores.model;

import java.io.Serializable;

public class Acao implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private String nome;
    private int qtd;

    public Acao(int id, int qtd) {
        this.id = id;
        this.qtd = qtd;
    }

    public String getNome() {
        return nome;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public int getQtd() {
        return this.qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }
    
    @Override
    public String toString() {
        return this.id + ";" + this.qtd;
    }

}