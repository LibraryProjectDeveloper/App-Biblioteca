package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.LateFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LateFeeRepository extends JpaRepository<LateFee,Long> {
    @Query("SELECT l FROM LateFee l WHERE l.loan.idLoan = ?1")
    boolean existsByLoanId(Long loanId);
}
