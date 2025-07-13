package com.WebBiblioteca.Repository;
import com.WebBiblioteca.Model.ReserveBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public interface ReserveBookRepository extends JpaRepository<ReserveBook, Long> {
    List<ReserveBook> findByState(boolean state);
    @Query("SELECT r FROM ReserveBook r WHERE r.user.code = ?1")
    List<ReserveBook> findByUserId(Long userId);
    @Query("SELECT r FROM ReserveBook r WHERE r.user.DNI = ?1")
    List<ReserveBook> findByUserDni(String dni);

    @Query("SELECT r FROM ReserveBook r JOIN r.book b WHERE b.title LIKE %?1%")
    List<ReserveBook> findByBookTitleContains(String title);

    List<ReserveBook> findByDateReserve(LocalDate dateReserve);
}

