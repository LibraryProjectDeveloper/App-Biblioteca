package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor,Long> {
    Optional<Autor> findByIdAutor(Long idAutor);
}
