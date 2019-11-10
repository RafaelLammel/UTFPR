package edu.utfpr.servidor.ultrassom.process;

import edu.utfpr.servidor.ultrassom.model.Imagem;
import edu.utfpr.servidor.ultrassom.repository.ImagemRepository;
import edu.utfpr.servidor.ultrassom.repository.UsuarioRepository;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ImageReconstruction implements Runnable {
   
    private Imagem imagem;
    private Vector g;
    private Matrix H;
    private Vector img;
    private int iteracoes;
    private JavaMailApp mail = new JavaMailApp();
    @Autowired
    ImagemRepository imagemRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    public void setImagem(Imagem i){
        this.imagem = i;
    }

    public void setData(double[] g){
        this.g = new DenseVector(g);
    }
    
    @Override
    public void run() {
        
        this.imagem.setDataInicio(new Date());
        this.imagem.setStatus(1);
        imagemRepository.save(this.imagem);
      
        this.H = new DenseMatrix(50816,3600);
        
        if (this.imagem.getAlgoritmo().equals("CGNE"))
            this.processCGNE();
        else
            this.processFISTA();
        
        this.buildImage();
        
        //mandar email aqui
    }
    
    private void buildImage(){
        double[] normalized = normalize();
        
        BufferedImage bf = new BufferedImage(60,60,BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = bf.getRaster();

        int k = 0;
        
        for(int i = 0; i < 60; i++){
            for(int j = 0; j < 60; j++){
                if(normalized[k] < 70)
                    raster.setSample(i, j, 0, 0.0);
                else
                    raster.setSample(i, j, 0, normalized[k]);
                k++;
            }
        }
        
        String path = "./imagens/";
        String diretorio = path+imagem.getUsuarioId();
        String fileName = imagem.getId()+".png";
        
        File directory = new File(diretorio);
        
        if(!directory.exists()){
            directory.mkdir();
        }
        
        File file = new File(diretorio+"/"+fileName);
        
        try {
            ImageIO.write(bf, "png", file);
        } catch (IOException ex) {
            Logger.getLogger(ImageReconstruction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.imagem.setIteracoes(iteracoes);
        this.imagem.setTamanho("60x60");
        this.imagem.setStatus(2);
        this.imagem.setCaminho_imagem(diretorio+"/"+fileName);
        this.imagem.setDataTermino(new Date());
        imagemRepository.save(this.imagem);
        mail.sendEmail(usuarioRepository.getEmailById(this.imagem.getUsuarioId())/*colar email do usuario aqui*/); //enviar email
    }
    
    private double[] normalize(){
        double[] normalized = new double[3600];
        double max = img.get(0);
        double min = img.get(0);
        
        for(int i = 1; i < 3600; i++){
            if(max < img.get(i))
                max = img.get(i);
            if(min > img.get(i))
                min = img.get(i);
        }
        
        for (int i = 0; i < 3600; i++)
            normalized[i] = ((img.get(i)-min)/(max-min))*255;
        
        return normalized;
    }
    
    private void processCGNE(){
        try {
            //Lendo a Matriz Modelo e guardando em uma matriz
            BufferedReader br = new BufferedReader(new FileReader("./modelos/H-1.txt"));
            StringBuffer s = new StringBuffer();
            int i = 0, j = 0;
            while(br.ready()){
                s = new StringBuffer();
                s.append(br.readLine());
                StringBuffer num = new StringBuffer();
                for(int k = 0; k < s.length(); k++){
                    if(s.charAt(k) == ','){
                        this.H.set(i, j, Double.parseDouble(num.toString()));
                        num = new StringBuffer();
                        j++;
                    }
                    else{
                        num.append(s.charAt(k));
                    }
                }
                this.H.set(i, j, Double.parseDouble(num.toString()));
                i++;
                j = 0;
            }
            
            //Iniciando o cálculo de reconstrução
            //r0 = g - Hf*0
            Vector f = new DenseVector(3600);
            f.zero();
            Vector fH = new DenseVector(50816);
            H.mult(f,fH);
            g.add(-1.0,fH);
            //(g virou r) p0 = Ht*r
            Vector p = new DenseVector(3600);
            H.transMult(g,p);
            double e = 0;
            do{
                //alpha = (rit*ri)/(pit*pi)
                double alpha = g.dot(g)/p.dot(p);
                //f = f + alpha*p
                f.add(alpha,p);
                //r = r - alpha*H*p (g = g - alpha*H*p)
                Vector Hp = new DenseVector(50816);
                Vector gAnterior = new DenseVector(g);
                H.mult(p, Hp);
                g.add(-alpha, Hp);
                //beta = rt*r/ri-1t*ri-1 (beta = gt*g/gi-1t*gi-1)
                double beta = g.dot(g)/gAnterior.dot(gAnterior);
                //p = Ht*r + beta*p (p = Ht*g + beta*p)
                Vector Htg = new DenseVector(3600);
                H.transMult(g, Htg);
                p.add(beta,Htg);
                
                //Calculo de erro
                e = g.norm(Vector.Norm.Two)-gAnterior.norm(Vector.Norm.Two);
                iteracoes++;
            }while(e < Math.pow(10,-4));
            img = new DenseVector(f);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImageReconstruction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ImageReconstruction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void processFISTA(){
        System.out.println("Ainda não implementado!");
    }
    
}
