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
    InterfaceCli referenciaCliente;

    public Transacao(int id, float preco, int qtd, InterfaceCli referenciaCliente){
        this.id = id;
        this.preco = preco;
        this.qtd = qtd;
        this.referenciaCliente = referenciaCliente;
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

}
