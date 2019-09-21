package edu.utfpr.servidor.ultrassom.repository;

import edu.utfpr.servidor.ultrassom.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    @Query(value = "SELECT * FROM usuario WHERE login = ?1 AND senha = ?2",
            nativeQuery=true)
    Usuario findByLogin(String login, String senha);
    
}
