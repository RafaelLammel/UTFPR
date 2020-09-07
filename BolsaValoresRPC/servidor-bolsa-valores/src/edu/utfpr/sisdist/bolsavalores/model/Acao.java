package edu.utfpr.sisdist.bolsavalores.model;

import java.io.Serializable;

public class Acao implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private String nome;
    private float preco;
    private int qtd;

    public Acao(int id, String nome, float preco, int qtd) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
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

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public int getId() {
        return id;
    }

    public int getQtd() {
        return this.qtd;
    }
    
}