package edu.utfpr.servidor.ultrassom.controller;

import edu.utfpr.servidor.ultrassom.model.Usuario;
import edu.utfpr.servidor.ultrassom.repository.UsuarioRepository;
import edu.utfpr.servidor.ultrassom.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {
    
    @Autowired
    UsuarioRepository usuarioRepository;
    
    @PostMapping("/cadastroUsuario")
    public Usuario cadastro(@RequestBody Usuario usuario){
        System.out.println("Hello There");
        return usuarioRepository.save(usuario);
    }
    
    @PostMapping("/login")
    public LoginResponse login(@RequestBody Usuario usuario){
        Usuario u = usuarioRepository.findByLogin(usuario.getLogin(), usuario.getSenha());
        if(u == null)
            return new LoginResponse(-1,false);
        return new LoginResponse(u.getId(),true);
    }
    
}
