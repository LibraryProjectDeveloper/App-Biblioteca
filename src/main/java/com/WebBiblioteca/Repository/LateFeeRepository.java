package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.LateFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LateFeeRepository extends JpaRepository<LateFee,Long> {
    @Query("SELECT COUNT(l) > 0 FROM LateFee l WHERE l.loan.idLoan = ?1")
    Boolean existsByLoanId(Long loanId);
    @Query("SELECT l FROM LateFee l WHERE l.loan.idLoan = ?1")
    Optional<LateFee> getLateFeeByLoan(Long loanId);
}
