package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.Book;
import com.WebBiblioteca.Model.BookState;
import com.WebBiblioteca.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookReposity extends JpaRepository<Book,Long> {
   Optional<Book> findByCodeBook(Long id);
   Optional<Book> findByIsbn(String isbn);
   List<Book> findByEstado(BookState estado);
   List<Book> findByCategory(Category category);

    @Query(value = "SELECT b FROM Book b WHERE year(b.publicationDate) = :anio")
    List<Book> findByPublicationDateYear(@Param("anio") Integer anio);
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByTitleContainingIgnoreCaseOrCategory(String title, Category category);
}
