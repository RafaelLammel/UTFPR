package edu.utfpr.servidor.ultrassom.repository;

import edu.utfpr.servidor.ultrassom.model.Imagem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagemRepository extends JpaRepository<Imagem,Integer>{
    
    Imagem findById(int id);
    
    List<Imagem> findByUsuarioId(int usuarioId);
    
}
