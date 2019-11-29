package edu.utfpr.servidor.ultrassom.controller;

import edu.utfpr.servidor.ultrassom.model.Imagem;
import edu.utfpr.servidor.ultrassom.process.ImageReconstruction;
import edu.utfpr.servidor.ultrassom.repository.ImagemRepository;
import edu.utfpr.servidor.ultrassom.repository.UsuarioRepository;
import edu.utfpr.servidor.ultrassom.request.ProcessRequest;
import edu.utfpr.servidor.ultrassom.response.ProcessResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
    
    @PostMapping("/process")
    public ProcessResponse recebeDados(@RequestBody ProcessRequest pr){
        Imagem i = new Imagem();
        i.setUsuarioId(pr.getUsuario_id());
        i.setAlgoritmo(pr.getAlgoritmo());
        i.setStatus(0);
        i.setTamanho(pr.getLargura()+"x"+pr.getAltura());
        i = imagemRepository.save(i);
        ProcessResponse prs = new ProcessResponse();
        System.out.println(Runtime.getRuntime().freeMemory());
        if(checkRecursos()){
            ImageReconstruction ir = new ImageReconstruction();
            ir.setS(pr.getAmostras());
            ir.setN(pr.getSensores());
            ir.setAltura(pr.getAltura());
            ir.setLargura(pr.getLargura());
            ir.setImagem(i);
            ir.setData(pr.getData());
            ir.setController(this);
            Thread tr = new Thread(ir);
            tr.start();
            System.out.println("Processando...");
            prs.setMensagem("Processando...");
        }
        else{ 
            storeOnQueue(i,pr.getLargura(),pr.getAltura(),pr.getAmostras(),pr.getSensores(),pr.getData());
            System.out.println("Na fila...");
            prs.setMensagem("Na fila...");
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
    
    public boolean checkRecursos(){
        //if(Runtime.getRuntime().freeMemory() >= 1147483647)
            return true;
        //return false;
    }
    
    public void storeOnQueue(Imagem i, int largura, int altura, int amostras, int sensores, double[] data){
        BufferedWriter writer = null;
        try {
            File directory = new File("./fila");
            String fileName = i.getId()+"-"+largura+"-"+altura+"-"+amostras+"-"+sensores+".txt";
            File file = new File(directory, fileName);
            writer = new BufferedWriter(new FileWriter(file));
            for(int j = 0; j < data.length; j++)
                writer.write(String.valueOf(data[j])+'\n');
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(ImagemController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getFromQueue(){
        if(checkRecursos()){
            System.out.println("Pegou da Fila!");
            File diretorio = new File("./fila");
            File[] files = diretorio.listFiles();
            File oldest = null;
            long oldestDate = Long.MAX_VALUE;
            if(files != null){
                for(File f : files){
                    if(f.lastModified() < oldestDate){
                        oldestDate = f.lastModified();
                        oldest = f;
                    }
                }
            }
            if(oldest != null){
                try {
                    
                    String[] chaves = new String[5];
                    
                    for(String c : chaves)
                        c = "";
                    
                    int j = 0;
                    int i = 0;
                    while(oldest.getName().charAt(i) != '.'){
                        if(oldest.getName().charAt(i) != '-')
                            chaves[j] += oldest.getName().charAt(i);
                        else{
                            chaves[j] = chaves[j].replace("null", "");
                            j++;
                        }
                        i++;
                    }
                    chaves[j] = chaves[j].replace("null", "");
                    
                    BufferedReader br = new BufferedReader(new FileReader("./fila/"+oldest.getName()));
                    double[] vetor = new double[Integer.parseInt(chaves[3])*Integer.parseInt(chaves[4])];
                    String line;
                    int k = 0;
                    while((line = br.readLine()) != null){
                        vetor[k] = Double.parseDouble(line.replace(",", "."));
                        k++;
                    }
                    br.close();
                    
                    Imagem im = imagemRepository.findById(Integer.parseInt(chaves[0]));
                    ImageReconstruction ir = new ImageReconstruction();
                    ir.setS(Integer.parseInt(chaves[3]));
                    ir.setN(Integer.parseInt(chaves[4]));
                    ir.setAltura(Integer.parseInt(chaves[2]));
                    ir.setLargura(Integer.parseInt(chaves[1]));
                    ir.setImagem(im);
                    ir.setData(vetor);
                    ir.setController(this);
                    Thread tr = new Thread(ir);
                    tr.start();
                    oldest.delete();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ImagemController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ImagemController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
