package edu.utfpr.servidor.ultrassom.controller;

import edu.utfpr.servidor.ultrassom.model.Imagem;
import edu.utfpr.servidor.ultrassom.process.ImageReconstruction;
import edu.utfpr.servidor.ultrassom.repository.ImagemRepository;
import edu.utfpr.servidor.ultrassom.request.ProcessRequest;
import edu.utfpr.servidor.ultrassom.response.ProcessResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImagemController {
    
    @Autowired
    ImagemRepository imagemRepository;
    
    @Autowired
    ImageReconstruction ir;
    
    @PostMapping("/process")
    public ProcessResponse recebeDados(@RequestBody ProcessRequest pr){
        Imagem i = new Imagem();
        i.setUsuarioId(pr.getUsuario_id());
        i.setAlgoritmo(pr.getAlgoritmo());
        i.setStatus(0);
        this.ir.setImagem(imagemRepository.save(i));
        this.ir.setData(pr.getData());
        Thread t = new Thread(ir);
        t.start();
        ProcessResponse prs = new ProcessResponse();
        prs.setMensagem("Processando");
        return prs;
    }
    
    @GetMapping("/imagem/{usuario_id}")
    public List<Imagem> getImagem(@PathVariable(value="usuario_id") int usuarioId){
        return imagemRepository.findByUsuarioId(usuarioId);
    }
    
}
