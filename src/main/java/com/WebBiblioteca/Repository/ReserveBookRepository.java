package com.WebBiblioteca.Repository;
import com.WebBiblioteca.Model.ReserveBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface ReserveBookRepository extends JpaRepository<ReserveBook, Long> {
    List<ReserveBook> findByState(boolean state);
}

