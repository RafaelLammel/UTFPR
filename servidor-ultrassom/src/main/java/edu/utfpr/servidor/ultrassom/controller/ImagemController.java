package edu.utfpr.servidor.ultrassom.controller;

import edu.utfpr.servidor.ultrassom.model.Imagem;
import edu.utfpr.servidor.ultrassom.process.ImageReconstruction;
import edu.utfpr.servidor.ultrassom.repository.ImagemRepository;
import edu.utfpr.servidor.ultrassom.repository.UsuarioRepository;
import edu.utfpr.servidor.ultrassom.request.ProcessRequest;
import edu.utfpr.servidor.ultrassom.response.ProcessResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
    
    @PostMapping("/process")
    public ProcessResponse recebeDados(@RequestBody ProcessRequest pr){
        Imagem i = new Imagem();
        i.setUsuarioId(pr.getUsuario_id());
        i.setAlgoritmo(pr.getAlgoritmo());
        i.setStatus(0);
        i.setTamanho(pr.getLargura()+"x"+pr.getAltura());
        ImageReconstruction ir = new ImageReconstruction();
        ir.setS(pr.getAmostras());
        ir.setN(pr.getSensores());
        ir.setAltura(pr.getAltura());
        ir.setLargura(pr.getLargura());
        ir.setImagem(imagemRepository.save(i));
        ir.setData(pr.getData());
        ir.setController(this);
        ProcessResponse prs = new ProcessResponse();
        System.out.println(Runtime.getRuntime().freeMemory());
        if(Runtime.getRuntime().freeMemory() >= 1073741824){
            System.out.println("Processando...");
            Thread tr = new Thread(ir);
            tr.start();
            prs.setMensagem("Processando...");
        }
        else{
            
            BufferedWriter writer = null;
            try {
                File directory = new File("./fila");
                String fileName = ir.getImagem().getId()+".txt";
                File file = new File(directory, fileName);
                writer = new BufferedWriter(new FileWriter(file));
                for(int j = 0; j < pr.getData().length; j++)
                    writer.write(String.valueOf(pr.getData()[j]));
                writer.close();
                System.out.println("Na fila...");
                prs.setMensagem("Na fila...");
            } catch (IOException ex) {
                Logger.getLogger(ImagemController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(ImagemController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
        return prs;
        //mail.sendEmail(usuarioRepository.getEmailById(this.imagem.getUsuarioId())/*colar email do usuario aqui*/); //enviar email
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
    
    public void updateImg(Imagem img){
        imagemRepository.save(img);
    }
    
}
