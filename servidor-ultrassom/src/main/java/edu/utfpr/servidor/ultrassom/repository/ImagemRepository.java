package edu.utfpr.servidor.ultrassom.repository;

import edu.utfpr.servidor.ultrassom.model.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagemRepository extends JpaRepository<Imagem,Integer>{
    
}
