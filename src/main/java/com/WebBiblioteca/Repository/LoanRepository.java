package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.Loan;
import com.WebBiblioteca.Model.LoanState;
import com.WebBiblioteca.Model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {
    @Query("SELECT l FROM Loan l WHERE l.state = ?1")
    Page<Loan> findByState(LoanState state, Pageable pageable);
    @Query("SELECT l FROM Loan l WHERE l.user.code = ?1")
    List<Loan> findByUserCode(Long userCode);
    @Query("SELECT l FROM Loan l where l.devolutionDate < ?1 and l.state = ?2")
    List<Loan> findByDevolutionDateBeforeAndState(LocalDateTime devolutionDate, LoanState state);
    @Query("SELECT l FROM Loan l WHERE l.user.DNI = ?1 and l.user.rol.nameRol = ?2")
    Page<Loan> findByUserDni(String dni, Role role,Pageable pageable);

    @Query("SELECT l FROM Loan l JOIN l.books b JOIN b.autores a where l.user.code = ?1 and b.title LIKE %?2% OR a.names LIKE %?2% OR a.lastname LIKE %?2%")
    List<Loan> findByBookTitleOrAuthorNameOrAuthorLastName(Long idUser,String searchTerm);

    List<Loan> findByStateAndUserCode(LoanState state, Long userCode);
    @Query("SELECT l FROM Loan l WHERE l.loanDate >= ?1 AND l.loanDate <= ?2 AND l.user.code = ?3")
    List<Loan> findByLoanDateAndUserCode(LocalDateTime dateStart,LocalDateTime dateEnd, Long userCode);

    @Query("SELECT l FROM Loan l WHERE l.loanDate BETWEEN ?1 AND ?2")
    List<Loan> findByLoanDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
