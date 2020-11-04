package utfpr.edu.sisdist.bolsavalores.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Acao {

    private int id;
    private int qtd;

    public Acao(int id, int qtd) {
        this.id = id;
        this.qtd = qtd;
    }

}
