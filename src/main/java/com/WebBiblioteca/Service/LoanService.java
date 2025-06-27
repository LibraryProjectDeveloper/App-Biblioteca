package com.WebBiblioteca.Service;

import com.WebBiblioteca.DTO.Autor.AuthorResponse;
import com.WebBiblioteca.DTO.Book.BookResponse;
import com.WebBiblioteca.DTO.Loan.LoanResponse;
import com.WebBiblioteca.Repository.LoanRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class LoanService {
    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository){
        this.loanRepository = loanRepository;
    }

    public List<LoanResponse> getAllLoans(){
        return loanRepository.findAll().stream().map(loan -> new LoanResponse(
                loan.getIdLoan(),
                loan.getLoanDate(),
                loan.getDevolutionDate(),
                loan.getState().name(),
                loan.getBooksQuantity(),
                loan.getUser().getCode(),
                loan.getUser().getName()+" "+loan.getUser().getLastname(),
                loan.getLibrarian().getCode(),
                loan.getLibrarian().getName()+" "+loan.getLibrarian().getLastname(),
                loan.getBooks().stream().map(book -> new BookResponse(
                        book.getCodeBook(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getPublicationDate(),
                        book.getPublisher(),
                        book.getCategory(),
                        book.getStockTotal(),
                        book.getEstado(),
                        book.getAutores().stream()
                                .map(author -> new AuthorResponse(
                                        author.getIdAuthor(),
                                        author.getNames(),
                                        author.getLastname(),
                                        author.getNationality(),
                                        author.getBirthdate(),
                                        author.getGender()
                                )).toList()
                )).toList()
        )).toList();
    }
    public List<LoanResponse> getLoansByState(String state){
        return loanRepository.findByState(state).stream().map(loan -> new LoanResponse(
                loan.getIdLoan(),
                loan.getLoanDate(),
                loan.getDevolutionDate(),
                loan.getState().name(),
                loan.getBooksQuantity(),
                loan.getUser().getCode(),
                loan.getUser().getName()+" "+loan.getUser().getLastname(),
                loan.getLibrarian().getCode(),
                loan.getLibrarian().getName()+" "+loan.getLibrarian().getLastname(),
                loan.getBooks().stream().map(book -> new BookResponse(
                        book.getCodeBook(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getPublicationDate(),
                        book.getPublisher(),
                        book.getCategory(),
                        book.getStockTotal(),
                        book.getEstado(),
                        book.getAutores().stream()
                                .map(author -> new AuthorResponse(
                                        author.getIdAuthor(),
                                        author.getNames(),
                                        author.getLastname(),
                                        author.getNationality(),
                                        author.getBirthdate(),
                                        author.getGender()
                                )).toList()
                )).toList()
        )).toList();
    }
    public List<LoanResponse> getLoansByUserId(Long userId) {
        return loanRepository.findByUserCode(userId).stream().map(loan -> new LoanResponse(
                loan.getIdLoan(),
                loan.getLoanDate(),
                loan.getDevolutionDate(),
                loan.getState().name(),
                loan.getBooksQuantity(),
                loan.getUser().getCode(),
                loan.getUser().getName()+" "+loan.getUser().getLastname(),
                loan.getLibrarian().getCode(),
                loan.getLibrarian().getName()+" "+loan.getLibrarian().getLastname(),
                loan.getBooks().stream().map(book -> new BookResponse(
                        book.getCodeBook(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getPublicationDate(),
                        book.getPublisher(),
                        book.getCategory(),
                        book.getStockTotal(),
                        book.getEstado(),
                        book.getAutores().stream()
                                .map(author -> new AuthorResponse(
                                        author.getIdAuthor(),
                                        author.getNames(),
                                        author.getLastname(),
                                        author.getNationality(),
                                        author.getBirthdate(),
                                        author.getGender()
                                )).toList()
                )).toList()
        )).toList();
    }
}
