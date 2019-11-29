package edu.utfpr.servidor.ultrassom.process;

import edu.utfpr.servidor.ultrassom.controller.ImagemController;
import edu.utfpr.servidor.ultrassom.model.Imagem;
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
import org.springframework.stereotype.Service;


@Service
public class ImageReconstruction implements Runnable{
   
    private Imagem imagem;
    private Vector g;
    private Matrix H;
    private Vector img;
    private int iteracoes;
    private int S;
    private int N;
    private int altura;
    private int largura;
    private ImagemController ic;

    public void setController(ImagemController ic){
        this.ic = ic;
    }
    
    public Imagem getImagem() {
        return imagem;
    }
    
    private JavaMailApp mail = new JavaMailApp();
    
    public void setImagem(Imagem i){
        this.imagem = i;
    }

    public void setData(double[] g){
        this.g = new DenseVector(g);
    }
    
    public void setS(int S) {
        this.S = S;
    }

    public void setN(int N) {
        this.N = N;
    }
    
     public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }
    
    @Override
    public void run() {
        
        this.imagem.setDataInicio(new Date());
        this.imagem.setStatus(1);
        ic.updateImg(this.imagem);
      
        this.H = new DenseMatrix(S*N,altura*largura);
        
        this.readModel();
        
        if (this.imagem.getAlgoritmo().equals("CGNE"))
            this.processCGNE();
        else
            this.processFISTA();
        
        this.buildImage();
        
        H = null;
        g = null;
        img = null;
        
        ic.getFromQueue();
        //mandar email aqui
    }
    
    private void buildImage(){
        double[] normalized = normalize();
        
        BufferedImage bf = new BufferedImage(largura,altura,BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = bf.getRaster();

        int k = 0;
        
        for(int i = 0; i < largura; i++){
            for(int j = 0; j < altura; j++){
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
        this.imagem.setStatus(2);
        this.imagem.setCaminho_imagem(diretorio+"/"+fileName);
        this.imagem.setDataTermino(new Date());
        ic.updateImg(this.imagem);
        //mail.sendEmail(usuarioRepository.getEmailById(this.imagem.getUsuarioId())/*colar email do usuario aqui*/); //enviar email
    }
    
    private double[] normalize(){
        double[] normalized = new double[altura*largura];
        double max = img.get(0);
        double min = img.get(0);
        
        for(int i = 1; i < altura*largura; i++){
            if(max < img.get(i))
                max = img.get(i);
            if(min > img.get(i))
                min = img.get(i);
        }
        
        for (int i = 0; i < altura*largura; i++)
            normalized[i] = ((img.get(i)-min)/(max-min))*255;
        
        return normalized;
    }
    
    private void processCGNE(){
        //Iniciando o cálculo de reconstrução
        //r0 = g - Hf*0
        Vector f = new DenseVector(altura*largura);
        f.zero();
        Vector fH = new DenseVector(S*N);
        H.mult(f,fH);
        g.add(-1.0,fH);
        //(g virou r) p0 = Ht*r
        Vector p = new DenseVector(altura*largura);
        H.transMult(g,p);
        double e = 0;
        do{
            //alpha = (rit*ri)/(pit*pi)
            double alpha = g.dot(g)/p.dot(p);
            //f = f + alpha*p
            f.add(alpha,p);
            //r = r - alpha*H*p (g = g - alpha*H*p)
            Vector Hp = new DenseVector(S*N);
            Vector gAnterior = new DenseVector(g);
            H.mult(p, Hp);
            g.add(-alpha, Hp);
            //beta = rt*r/ri-1t*ri-1 (beta = gt*g/gi-1t*gi-1)
            double beta = g.dot(g)/gAnterior.dot(gAnterior);
            //p = Ht*r + beta*p (p = Ht*g + beta*p)
            Vector Htg = new DenseVector(altura*largura);
            H.transMult(g, Htg);
            p.add(beta,Htg);

            //Calculo de erro
            e = g.norm(Vector.Norm.Two)-gAnterior.norm(Vector.Norm.Two);
            iteracoes++;
        }while(e < Math.pow(10,-4));
        img = new DenseVector(f);
    }
    
    private void processFISTA(){
        //Iniciando calculo de reconstrução
        Vector f = new DenseVector(altura*largura);
        f.zero();
        Vector y = new DenseVector(f);
        double alpha = 1;
        double e = 0;
        do{
            
            Vector fAnterior = new DenseVector(f);
            Vector Hy = new DenseVector(S*N);
            //Hy = H*y
            H.mult(y, Hy);
            Vector gSub = new DenseVector(g);
            //gSub = g - Hy
            gSub.add(-1,Hy);
            Vector transp = new DenseVector(altura*largura);
            //transp = Ht*gSub
            H.transMult(gSub, transp);
            //c = |Ht*H|2
            double c = H.norm(Matrix.Norm.Frobenius);
            //trasnp = transp * (1/c)
            transp.scale(1/c);
            //transp = transp + y
            transp.add(y);
            
            //lambda = max(abs(Ht*g)) * 0.10;
            Vector Htg = new DenseVector(altura*largura);
            H.transMult(g, Htg);
            double lambda = Htg.norm(Vector.Norm.Infinity)* 0.1;
            
            f = S(lambda/c,transp.norm(Vector.Norm.Infinity), transp);
            
            
            //alpha = (1+raiz(1+4*alpha^2))/2
            double alphaAnterior = alpha;
            alpha = (1+Math.sqrt(1+(4*alpha*alpha)))/2;
            
            //y = f + ((alphaAnteior-1)/alpha) * (f-fAnterior)
            Vector yAnterior = new DenseVector(y);
            Vector fSub = new DenseVector(f);
            fSub.add(-1,fAnterior);
            y = f.copy();
            y.add(((alphaAnterior-1)/(alpha)),fSub);
            
            //Calculo do erro
            e = y.norm(Vector.Norm.Two)-yAnterior.norm(Vector.Norm.Two);
            iteracoes++;
        }while(e < Math.pow(10, -4));
        img = new DenseVector(f);
    }
    
    private DenseVector S(double a, double x, Vector xVetor){
        if(a >= x)
            return new DenseVector(altura*largura).zero();
        for(int i = 0; i < xVetor.size(); i++){
            double val = 0;
            if(xVetor.get(i) < a)
                val = -1;
            if(xVetor.get(i) > a)
                val = 1;
            xVetor.add(i, val);
        }
        return new DenseVector(xVetor);
    }
    
    private void readModel(){
        BufferedReader br = null;
        try {
            //Lendo a Matriz Modelo e guardando em uma matriz
            br = new BufferedReader(new FileReader("./modelos/"+largura+"x"+altura+" - S="+S+" - N="+N+".txt"));
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
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImageReconstruction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ImageReconstruction.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(ImageReconstruction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
