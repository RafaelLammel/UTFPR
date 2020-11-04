package edu.utfpr.sisdist.bolsavalores.model;

import java.io.Serializable;

import edu.utfpr.sisdist.bolsavalores.remote.InterfaceCli;

public class Transacao implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private float preco;
    private int qtd;
    private long delay;
    private boolean efetuado;
    InterfaceCli referenciaCliente;

    public Transacao(int id, float preco, int qtd, long delay, InterfaceCli referenciaCliente) {
        this.id = id;
        this.preco = preco;
        this.qtd = qtd;
        this.delay = delay;
        this.referenciaCliente = referenciaCliente;
        this.efetuado = false;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public int getId() {
        return this.id;
    }

    public float getPreco() {
        return this.preco;
    }

    public int getQtd() {
        return this.qtd;
    }

    public InterfaceCli getReferenciaCliente() {
		return this.referenciaCliente;
	}

    public void setEfetuado(boolean efetuado) {
        this.efetuado = efetuado;
    }

    public boolean getEfetuado() {
        return this.efetuado;
    }

}
