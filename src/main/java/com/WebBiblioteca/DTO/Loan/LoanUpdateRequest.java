package com.WebBiblioteca.DTO.Loan;

import com.WebBiblioteca.Model.LoanState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter @AllArgsConstructor @NoArgsConstructor
public class LoanUpdateRequest {
    private Integer booksQuantity;
    private Long userId;
    private Long librarianId;
    private List<Long> bookIds;
    private Integer loanDays;
    private LoanState state;
}
