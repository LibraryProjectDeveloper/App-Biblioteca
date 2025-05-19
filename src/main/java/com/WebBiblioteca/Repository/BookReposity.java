package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.Book;
import com.WebBiblioteca.Model.EstadoBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookReposity extends JpaRepository<Book,Long> {
   Optional<Book> findByCodeBook(Long id);
   Optional<Book> findByIsbn(String isbn);

   List<Book> findByEstado(EstadoBook estado);
}
