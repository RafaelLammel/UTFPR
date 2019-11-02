package edu.utfpr.servidor.ultrassom.process;

import edu.utfpr.servidor.ultrassom.model.Imagem;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

public class ImageReconstruction implements Runnable {
   
    private Imagem imagem;
    private Vector g;
    private Matrix H;
    private Vector img;
    private int iteracoes;
    
    public ImageReconstruction(Imagem imagem, double[] g){
        this.imagem = imagem;
        this.g = new DenseVector(g);
        this.H = new DenseMatrix(50816,3600);
    }
    
    @Override
    public void run() {
        if (this.imagem.getAlgoritmo().equals("CGNE"))
            this.processCGNE();
        else
            this.processFISTA();
        
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
        
        BufferedImage bf = new BufferedImage(60,60,BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = bf.getRaster();
        
        //byte[] newData = ((DataBufferByte) bf.getRaster().getDataBuffer()).getData(); 
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
        
        File file = new File("./saida.bmp");
        
        String content = "";
        for(int i = 1; i <= 60; i++){
            for(int j = 1; j <= 60; j++){
                content += normalized[(i*j)-1] + " - ";
            }
            content+="\n";
        }

        // If the file doesn't exists, create and write to it
		// If the file exists, truncate (remove all content) and write to it
        try (FileWriter writer = new FileWriter("app.txt");
             BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(content);

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        
        try {
            ImageIO.write(bf, "bmp", file);
        } catch (IOException ex) {
            Logger.getLogger(ImageReconstruction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("FINISH");
        
    }
    
    private void processCGNE(){
        try {
            //Lendo a Matriz Modelo e guardando em uma matriz
            BufferedReader br = new BufferedReader(new FileReader("./H-1.txt"));
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
