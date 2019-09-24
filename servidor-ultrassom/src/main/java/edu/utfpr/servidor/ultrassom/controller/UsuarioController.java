package edu.utfpr.servidor.ultrassom.controller;

import edu.utfpr.servidor.ultrassom.model.Usuario;
import edu.utfpr.servidor.ultrassom.repository.UsuarioRepository;
import edu.utfpr.servidor.ultrassom.request.LoginRequest;
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
        return usuarioRepository.save(usuario);
    }
    
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        Usuario u = usuarioRepository.findByLoginAndSenha(loginRequest.getLogin(), loginRequest.getSenha());
        if(u == null)
            return new LoginResponse(-1, null, null, null);
        return new LoginResponse(u.getId(),u.getNome(),u.getLogin(),u.getEmail());
    }
    
}
