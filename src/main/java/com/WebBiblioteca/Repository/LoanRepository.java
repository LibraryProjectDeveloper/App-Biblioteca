package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.Loan;
import com.WebBiblioteca.Model.LoanState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {
    @Query("SELECT l FROM Loan l WHERE l.state = ?1")
    Optional<Loan> findByState(LoanState state);
    @Query("SELECT l FROM Loan l WHERE l.user.code = ?1")
    Optional<Loan> findByUserCode(Long userCode);
    @Query("SELECT l FROM Loan l where l.devolutionDate < ?1 and l.state = ?2")
    List<Loan> findByDevolutionDateBeforeAndState(LocalDateTime devolutionDate, LoanState state);
    @Query("SELECT l FROM Loan l WHERE l.user.DNI = ?1")
    List<Loan> findByUserDni(String dni);
}
