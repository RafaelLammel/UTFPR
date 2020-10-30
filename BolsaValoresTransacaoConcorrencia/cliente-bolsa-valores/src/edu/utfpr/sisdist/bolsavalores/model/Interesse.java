package edu.utfpr.sisdist.bolsavalores.model;

import java.io.Serializable;

public class Interesse implements Serializable{
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private float teto;
    private float piso;
    private float ultimoValor;

    public Interesse(int id, float teto, float piso) {
        this.id = id;
        this.teto = teto;
        this.piso = piso;
        this.ultimoValor = -1;
    }

    public int getId() {
        return this.id;
    }

    public float getTeto() {
        return this.teto;
    }

    public float getPiso() {
        return this.piso;
    }

    public float getUltimoValor() {
        return this.ultimoValor;
    }

    public void setUltimoValor(float ultimoValor) {
        this.ultimoValor = ultimoValor;
    }

}
