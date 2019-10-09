package edu.utfpr.servidor.ultrassom.process;

import edu.utfpr.servidor.ultrassom.model.Imagem;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.uib.cipr.matrix.DenseMatrix;

public class ImageReconstruction implements Runnable {
   
    private Imagem imagem;
    private Double[] g;
    private DenseMatrix H;
    
    public ImageReconstruction(Imagem imagem, Double[] g){
        this.imagem = imagem;
        this.g = g;
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
            System.out.println("Items na matriz!");
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
