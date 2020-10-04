package utfpr.edu.sisdist.bolsavalores.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.web.bind.annotation.*;

import utfpr.edu.sisdist.bolsavalores.model.Acao;
import utfpr.edu.sisdist.bolsavalores.model.Cliente;
import utfpr.edu.sisdist.bolsavalores.model.Cotacao;
import utfpr.edu.sisdist.bolsavalores.model.Interesse;
import utfpr.edu.sisdist.bolsavalores.util.Utilidades;

@RestController
@RequestMapping("cliente")
@CrossOrigin(origins = "*")
public class ClienteController {

    @PostMapping
    public int adicionaCliente() {
        Cliente cliente = new Cliente();
        cliente.addAcao(Utilidades.getInstance().getIdAcaoAtual(), 50);
        cliente.addAcao(Utilidades.getInstance().getIdAcaoAtual()+1, 50);
        Utilidades.getInstance().setIdAcaoAtual(Utilidades.getInstance().getIdAcaoAtual()+2);
        Utilidades.getInstance().getClientes().add(cliente);
        cliente.setId(Utilidades.getInstance().getIdAtual());
        Utilidades.getInstance().setIdAtual(Utilidades.getInstance().getIdAtual()+1);
        Random gerador = new Random();
        for (Acao acao : cliente.getCarteira()){
            Utilidades.getInstance().getAcoes().put(acao.getId(), (float)gerador.nextInt(100));
        }
        return cliente.getId();
    }

    @GetMapping("/{id}/carteira")
    public List<Acao> getCarteira(@PathVariable("id") int id) {
        Optional<Cliente> cliente = Utilidades.getInstance().getClientes().stream().filter(x -> x.getId() == id).findFirst();
        if(cliente.isPresent()) {
            return cliente.get().getCarteira();
        }
        return null;
    }

    @PostMapping("/{id}/cotacao")
    public void registrarCotacao(@RequestBody Cotacao cotacao, @PathVariable("id") int id) {
        Optional<Cliente> cliente = Utilidades.getInstance().getClientes().stream()
            .filter(x -> x.getId() == id).findFirst();
        if(cliente.isPresent()) {
            for(Integer acao : Utilidades.getInstance().getAcoes().keySet()) {
                if(acao == id) {
                    cliente.get().registrarCotacao(id);
                }
            }
        }
    }

    @DeleteMapping("/{id}/cotacao/{idCotacao}")
    public void removeCotacao(@PathVariable("id") int id, @PathVariable("idCotacao") int idCotacao) {
        Optional<Cliente> cliente = Utilidades.getInstance().getClientes().stream()
            .filter(x -> x.getId() == id).findFirst();
        if(cliente.isPresent()) {
            cliente.get().getCotacoes().remove(Integer.valueOf(idCotacao));
        }
    }

    @GetMapping("/{id}/cotacao")
    public Map<Integer, Float> listaCotacao(@PathVariable("id") int id) {
        Optional<Cliente> cliente = Utilidades.getInstance().getClientes().stream()
            .filter(x -> x.getId() == id).findFirst();
        if(cliente.isPresent()) {
            Map<Integer, Float> c = new HashMap<Integer, Float>();
            for(Integer acao : cliente.get().getCotacoes()) {
                c.put(acao, Utilidades.getInstance().getAcoes().get(acao));
            }
            return c;
        }
        return null;
    }

    @GetMapping("/{id}/interesse")
    public List<Interesse> listaInteresse(@PathVariable("id") int id) {
        Optional<Cliente> cliente = Utilidades.getInstance().getClientes().stream()
                .filter(x -> x.getId() == id).findFirst();
        return cliente.get().getInteresses();
    }

    @PostMapping("/{id}/interesse")
    public void registraInteresse(@RequestBody Interesse interesse, @PathVariable("id") int id) {
        interesse.setUltimoValor(-1);
        Optional<Cliente> cliente = Utilidades.getInstance().getClientes().stream()
                .filter(x -> x.getId() == id).findFirst();
        if(cliente.isPresent()) {
            cliente.get().getInteresses().add(interesse);
            Utilidades.getInstance().notificaMargem();
        }
        else {
            System.out.println("Cliente não encontrado");
        }
    }

}
