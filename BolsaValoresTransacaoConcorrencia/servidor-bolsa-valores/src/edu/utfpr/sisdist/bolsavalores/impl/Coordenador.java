package edu.utfpr.sisdist.bolsavalores.impl;

import edu.utfpr.sisdist.bolsavalores.model.Cliente;
import edu.utfpr.sisdist.bolsavalores.model.Transacao;
import edu.utfpr.sisdist.bolsavalores.remote.InterfaceCo;

public class Coordenador implements InterfaceCo {

    private String status = "NÃO INICIADA";

    @Override
    public void abrirTransacao(Transacao compra, Transacao venda, Cliente comprador, Cliente vendedor) {
        // Gravar log Transação iniciada
        this.status = "INICIADA";
        if(comprador.preparar(venda, 0) && vendedor.preparar(compra, 1)) {
            // Gravar log Transação preparada
            this.status = "PREPARADA";
            if(comprador.efetuar() && vendedor.efetuar()) {
                // Gravar Log finalizada
                this.status = "EFETUADA";
            }
            else{
                this.status = "ABORTADA";
                comprador.abortar();
                vendedor.abortar();
                // Gravar log transação abortada
            }
        }
        else{
            this.status = "ABORTADA";
            comprador.abortar();
            vendedor.abortar();
            // Gravar log transação abortada
        }
    }

    @Override
    public String obterEstadoTransacao() {
        return status;
    }
    
}
