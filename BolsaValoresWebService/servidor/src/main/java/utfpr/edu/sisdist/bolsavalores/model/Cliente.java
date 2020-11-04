package utfpr.edu.sisdist.bolsavalores.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cliente {
    
    private int id;
    private List<Acao> carteira;
    private List<Integer> cotacoes;
    private List<Interesse> interesses;

    public Cliente() {
        this.carteira = new ArrayList<>();
        this.cotacoes = new ArrayList<>();
        this.interesses = new ArrayList<>();
    }

    public synchronized void addAcao(int id, int qtd) {
        Optional<Acao> acao = this.carteira.stream().filter(x -> x.getId() == id).findFirst();
        if(acao.isPresent()) {
            acao.get().setQtd(qtd + acao.get().getQtd());
        }
        else {
            carteira.add(new Acao(id, qtd));
        }
    }

    public synchronized void removeAcao(int id, int qtd) {
        Optional<Acao> acao = this.carteira.stream().filter(x -> x.getId() == id).findFirst();
        int novaQtd = acao.get().getQtd() - qtd;
        if(novaQtd == 0) {
            carteira.remove(acao.get());
        }
        else {
            acao.get().setQtd(novaQtd);
        }
    }

    public void registrarCotacao(int id) {
        this.cotacoes.add(id);
    }

}
