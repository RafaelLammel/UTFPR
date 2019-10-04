package edu.utfpr.servidor.ultrassom.process;

import edu.utfpr.servidor.ultrassom.model.Imagem;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageReconstruction implements Runnable {
   
    private Imagem imagem;
    private Double[] g;
    
    public ImageReconstruction(Imagem imagem, Double[] g){
        this.imagem = imagem;
        this.g = g;
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
            while(br.ready()){
                System.out.println(br.readLine());
            }
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
