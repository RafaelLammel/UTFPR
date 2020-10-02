package utfpr.edu.sisdist.bolsavalores.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import utfpr.edu.sisdist.bolsavalores.model.Cliente;
import utfpr.edu.sisdist.bolsavalores.model.Transacao;

@Getter
@Setter
public class Utilidades {

    private static Utilidades INSTANCIA = null;

    private int idAtual;
    private int idAcaoAtual;
    private Map<Integer, Float> acoes;
    private List<Cliente> clientes;
    private List<Transacao> compras;
    private List<Transacao> vendas;

    private Utilidades() {
        idAcaoAtual = 1;
        idAtual = 1;
        acoes = new HashMap<>();
        clientes = new ArrayList<>();
        compras = new ArrayList<>();
        vendas = new ArrayList<>();
    }

    public static Utilidades getInstance() {
        if (INSTANCIA == null) {
            synchronized (Utilidades.class) {
                if (INSTANCIA == null) {
                    INSTANCIA = new Utilidades();
                }
            }
        }
        return INSTANCIA;
    }

}
