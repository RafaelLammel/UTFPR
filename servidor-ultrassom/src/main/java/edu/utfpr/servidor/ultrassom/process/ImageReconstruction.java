package edu.utfpr.servidor.ultrassom.process;

import edu.utfpr.servidor.ultrassom.model.Imagem;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

public class ImageReconstruction implements Runnable {
   
    private Imagem imagem;
    private Vector g;
    private Matrix H;
    
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
            Vector fH = null;
            H.mult(f,fH);
            Vector r = null;
            g.add(-1.0,fH);
            //(g virou r) p0 = Ht*r
            Matrix Ht = null;
            H.transpose(Ht);
            Vector p = null;
            Ht.mult(g,p);
            double e = 0;
            do{
                //alpha = (rit*ri)/(pit*pi)
                double alpha = g.dot(g)/p.dot(p);
                //f = f + alpha*p
                f.add(alpha,p);
                //r = r - alpha*H*p (g = g - alpha*H*p)
                Vector Hp = null;
                Vector gAnterior = g.copy();
                H.mult(p, Hp);
                g.add(-alpha, Hp);
                //beta = rt*r/ri-1t*ri-1 (beta = gt*g/gi-1t*gi-1)
                double beta = g.dot(g)/gAnterior.dot(gAnterior);
                //p = Ht*r + beta*p (p = Ht*g + beta*p)
                Vector Htg = null;
                Ht.mult(g, Htg);
                p.add(beta,Htg);
                
                //Calculo de erro
                e = g.norm(Vector.Norm.Two)-gAnterior.norm(Vector.Norm.Two);
            }while(e < Math.pow(10,-4));
            System.out.println("FOI MEU DEUS");
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
