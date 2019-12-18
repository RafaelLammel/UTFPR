package edu.utfpr.servidor.ultrassom.repository;

import edu.utfpr.servidor.ultrassom.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    Usuario findByLoginAndSenha(String login, String senha);
    
    @Query(value="SELECT email FROM usuario WHERE id = :id", nativeQuery=true)
    String getEmailById(@Param("id")int id);
    
}
