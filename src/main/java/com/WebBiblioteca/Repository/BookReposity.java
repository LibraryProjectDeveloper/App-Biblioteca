package com.WebBiblioteca.Repository;
import com.WebBiblioteca.Model.Book;
import com.WebBiblioteca.Model.BookState;
import com.WebBiblioteca.Model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookReposity extends JpaRepository<Book,Long> {
   Optional<Book> findByCodeBook(Long id);
   Optional<Book> findByIsbn(String isbn);


   Page<Book> findByEstado(BookState estado, Pageable pageable);


   Page<Book> findByCategory(Category category, Pageable pageable);

    @Query(value = "SELECT b FROM Book b WHERE year(b.publicationDate) = :anio")
    List<Book> findByPublicationDateYear(@Param("anio") Integer anio);
    List<Book> findByTitleContainingIgnoreCase(String title);

    @Query(
            value = "CALL sp_get_popular_books(:p_date_start,:p_date_end,:p_category)",
            nativeQuery = true
    )
    List<Object[]> getPopularBooks(
            @Param("p_date_start") LocalDate dateStart,
            @Param("p_date_end") LocalDate dateEnd,
            @Param("p_category") String category
    );

    @Query(value = "SELECT b FROM Book b WHERE b.stockTotal > 0 AND b.estado = :estado")
    List<Book> findAvailableBooksByState(@Param("estado") BookState estado);

    @Query("SELECT b FROM Book b join b.autores a WHERE a.names LIKE %?1% OR a.lastname LIKE %?1%")
    List<Book> searchAutor(@Param("authorName") String authorName);

    @Query(value = "SELECT b.category, COUNT(r.idLoan) FROM Book b JOIN b.loans r WHERE r.state = 'PRESTADO' GROUP BY b.category")
    List<Object[]> countBooksLoanedByCategory();
}
