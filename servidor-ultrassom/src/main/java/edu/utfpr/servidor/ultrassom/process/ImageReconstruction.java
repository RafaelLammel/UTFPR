package edu.utfpr.servidor.ultrassom.process;

import edu.utfpr.servidor.ultrassom.repository.ImagemRepository;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class ImageReconstruction implements Runnable {
    
    @Autowired
    ImagemRepository imagemRepository;
    
    @Override
    public void run() {
        try {
            ArrayList<Double> modelo = new ArrayList();
            BufferedReader br = new BufferedReader(new FileReader("./Modelo-1.txt"));
            String s;
            String num = "";
            while((s = br.readLine()).length() != 0){
                for(int i = 0; i < s.length(); i++){
                    if(s.charAt(i) == ','){
                        modelo.add(Double.parseDouble(num));
                        num = "";
                    }
                    else
                        num += s.charAt(i);
                }
                modelo.add(Double.parseDouble(num));
                num = "";
            }
            System.out.println(modelo.toString());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImageReconstruction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ImageReconstruction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
