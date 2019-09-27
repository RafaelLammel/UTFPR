package edu.utfpr.servidor.ultrassom.controller;

import edu.utfpr.servidor.ultrassom.request.ProcessRequest;
import edu.utfpr.servidor.ultrassom.response.ProcessResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImagemController {
    
    @PostMapping("/process")
    public ProcessResponse recebeDados(@RequestBody ProcessRequest i){
        System.out.println(i.getData());
        ProcessResponse pr = new ProcessResponse();
        pr.setMensagem("Deu boa!");
        return pr;
    }
    
}
