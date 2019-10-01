package edu.utfpr.servidor.ultrassom.controller;

import edu.utfpr.servidor.ultrassom.model.Imagem;
import edu.utfpr.servidor.ultrassom.process.ImageReconstruction;
import edu.utfpr.servidor.ultrassom.repository.ImagemRepository;
import edu.utfpr.servidor.ultrassom.request.ProcessRequest;
import edu.utfpr.servidor.ultrassom.response.ProcessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImagemController {
    
    @Autowired
    ImagemRepository imagemRepository;
    
    @PostMapping("/process")
    public ProcessResponse recebeDados(@RequestBody ProcessRequest pr){
        Imagem i = new Imagem();
        i.setUsuario_id(pr.getUsuario_id());
        i.setAlgoritmo(pr.getAlgoritmo());
        i.setStatus(0);
        ImageReconstruction ir = new ImageReconstruction();
        Thread t = new Thread(ir);
        t.start();
        imagemRepository.save(i);
        ProcessResponse prs = new ProcessResponse();
        prs.setMensagem("Deu boa!");
        return prs;
    }
    
}
