package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Long> {
    Optional<Author> findByIdAuthor(Long idAuthor);
    List<Author> findByNamesContainingIgnoreCase(String names);
    List<Author> findByLastnameContainingIgnoreCase(String lastname);
    List<Author> findByNamesContainingIgnoreCaseOrLastnameContainingIgnoreCase(String names, String lastname);
}
