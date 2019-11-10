package edu.utfpr.servidor.ultrassom.controller;

import edu.utfpr.servidor.ultrassom.model.Imagem;
import edu.utfpr.servidor.ultrassom.process.ImageReconstruction;
import edu.utfpr.servidor.ultrassom.repository.ImagemRepository;
import edu.utfpr.servidor.ultrassom.repository.UsuarioRepository;
import edu.utfpr.servidor.ultrassom.request.ProcessRequest;
import edu.utfpr.servidor.ultrassom.response.ProcessResponse;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImagemController {
    
    @Autowired
    ImagemRepository imagemRepository;
    
    @Autowired
    UsuarioRepository usuarioRepository;
    
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
    public List<Imagem> listImagem(@PathVariable(value="usuario_id") int usuarioId){
        return imagemRepository.findByUsuarioId(usuarioId);
    }
    
    @GetMapping("/imagem/{usuario_id}/{imagem_id}")
    @ResponseBody
    public void getImagem(@PathVariable("usuario_id") int usuarioId, 
                          @PathVariable("imagem_id") int imagemId,
                          HttpServletResponse response){
       response.setContentType("application/png");
       
        try {
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            FileInputStream fis = new FileInputStream("./imagens/"+usuarioId+"/"+imagemId+".png");
            int len;
            byte[] buf = new byte[1024];
            while((len = fis.read(buf)) > 0){
                bos.write(buf,0,len);
            }
            bos.close();
            response.flushBuffer();
        } catch (IOException ex) {
            Logger.getLogger(ImagemController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
}
