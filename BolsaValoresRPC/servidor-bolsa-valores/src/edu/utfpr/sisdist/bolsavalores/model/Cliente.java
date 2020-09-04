package edu.utfpr.sisdist.bolsavalores.model;

import java.util.ArrayList;
import java.util.List;

import edu.utfpr.sisdist.bolsavalores.remote.InterfaceCli;

public class Cliente {

    private InterfaceCli interfaceCli;
    private List<Acao> carteira;
    private List<Integer> interesses;

    public Cliente(InterfaceCli interfaceCli) {
        this.interfaceCli = interfaceCli;
        carteira = new ArrayList<>();
        interesses = new ArrayList<>();
    }
    
    public InterfaceCli getinterfaceCli() {
        return interfaceCli;
    }

    public List<Acao> getAcoes() {
        return carteira;
    }

    public List<Integer> getInteresses() {
        return interesses;
    }

}
