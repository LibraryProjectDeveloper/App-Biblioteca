package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {
    @Query("SELECT l FROM Loan l WHERE l.state = ?1")
    Optional<Loan> findByState(String state);
    @Query("SELECT l FROM Loan l WHERE l.user.code = ?1")
    Optional<Loan> findByUserCode(Long userCode);
}
