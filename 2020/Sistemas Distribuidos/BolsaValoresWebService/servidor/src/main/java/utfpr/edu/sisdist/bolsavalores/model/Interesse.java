package utfpr.edu.sisdist.bolsavalores.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Interesse {

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

}
