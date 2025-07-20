package com.WebBiblioteca.Repository;
import com.WebBiblioteca.Model.ReserveBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(
           value = "CALL sp_get_history_reservation(:p_date_start,:p_date_end)",
           nativeQuery = true
    )
    List<Object[]> getHistoryReservation(
            @Param("p_date_start")LocalDate date_start,
            @Param("p_date_end")LocalDate date_end
            );

    @Query("select r from ReserveBook as r join r.book l join l.autores as la where l.title  like %?1% or la.names like %?1% and r.user.code = ?2 ")
    List<ReserveBook> findByBookTitleOrAuthorNameOrAuthorLastName(String query, Long userId);

    List<ReserveBook> findByDateReserveAndUserCode(LocalDate dateReserve, Long userCode);

    List<ReserveBook> findByStateAndUserCode(boolean state, Long userCode);
}

