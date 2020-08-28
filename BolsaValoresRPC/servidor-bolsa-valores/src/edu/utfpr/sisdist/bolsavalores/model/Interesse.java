package edu.utfpr.sisdist.bolsavalores.model;

import edu.utfpr.sisdist.bolsavalores.remote.InterfaceCli;

public class Interesse {

    private int idAcao;
    private InterfaceCli interfaceCli;

    public Interesse(int idAcao, InterfaceCli interfaceCli) {
        this.idAcao = idAcao;
        this.interfaceCli = interfaceCli;
    }

    public InterfaceCli getInterfaceCli() {
        return interfaceCli;
    }

    public void setInterfaceCli(InterfaceCli interfaceCli) {
        this.interfaceCli = interfaceCli;
    }

    public int getIdAcao() {
        return idAcao;
    }

}