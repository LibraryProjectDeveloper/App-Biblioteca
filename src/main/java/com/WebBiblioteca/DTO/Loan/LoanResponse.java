package com.WebBiblioteca.DTO.Loan;

import com.WebBiblioteca.DTO.Book.BookResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter @AllArgsConstructor
public class LoanResponse {
    private Long idLean;
    private LocalDateTime loanDate;
    private LocalDateTime devolutionDate;
    private String state;
    private int booksQuantity;
    private Long userId;
    private String username;
    private Long librarian;
    private String librarianName;
    private List<BookResponse> bookResponseList;
}
