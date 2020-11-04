package utfpr.edu.sisdist.bolsavalores.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Transacao {
    
    private int id;
    private int idCliente;
    private float preco;
    private int qtd;
    private long delay;

}
