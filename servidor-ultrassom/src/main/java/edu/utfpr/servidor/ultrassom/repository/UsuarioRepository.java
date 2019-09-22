package edu.utfpr.servidor.ultrassom.repository;

import edu.utfpr.servidor.ultrassom.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    Usuario findByLoginAndSenha(String login, String senha);
    
}
