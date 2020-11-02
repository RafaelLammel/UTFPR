package edu.utfpr.sisdist.bolsavalores.remote;

import edu.utfpr.sisdist.bolsavalores.model.Cliente;
import edu.utfpr.sisdist.bolsavalores.model.Transacao;

public interface InterfaceCo {

    void abrirTransacao(Transacao compra, Transacao venda, Cliente comprador, Cliente vendedor);

    String obterEstadoTransacao();

}