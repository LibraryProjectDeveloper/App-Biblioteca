package com.WebBiblioteca.Service;

import com.WebBiblioteca.DTO.Autor.AuthorResponse;
import com.WebBiblioteca.DTO.Book.BookResponse;
import com.WebBiblioteca.DTO.Loan.LoanRequest;
import com.WebBiblioteca.DTO.Loan.LoanResponse;
import com.WebBiblioteca.DTO.Loan.LoanUpdateRequest;
import com.WebBiblioteca.Exception.ResourceNotFoundException;
import com.WebBiblioteca.Model.Book;
import com.WebBiblioteca.Model.Loan;
import com.WebBiblioteca.Model.LoanState;
import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserService userService;
    private final BookService bookService;

    public LoanService(LoanRepository loanRepository, UserService userService, BookService bookService) {
        this.loanRepository = loanRepository;
        this.userService = userService;
        this.bookService = bookService;
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

    public List<LoanResponse> getLoansByState(LoanState state){
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
    public LoanResponse getLoanById(Long id) {
        return loanRepository.findById(id)
                .map(loan -> new LoanResponse(
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
                )).orElseThrow(() -> new ResourceNotFoundException("Loan", "id", id));
    }
    @Transactional
    public LoanResponse saveLoan(LoanRequest loanRequest){
        User user = userService.getUser(loanRequest.getUserId());
        User librarian = userService.getUser(loanRequest.getLibrarianId());
        if(bookService.verifyBooks(loanRequest.getBookIds())){
            Set<Book> books = bookService.updateBooksStock(bookService.getBooksByIds(loanRequest.getBookIds()));
            Loan loan = new Loan();
            loan.setBooksQuantity(loanRequest.getBooksQuantity());
            loan.setUser(user);
            loan.setLibrarian(librarian);
            loan.setBooks(books);
            loan.setLoanDate(LocalDateTime.now());
            loan.setDevolutionDate(loan.getLoanDate().plusDays(loanRequest.getLoanDays()));
            loan.setState(LoanState.PRESTADO);
            Loan savedLoan = loanRepository.save(loan);
            return new LoanResponse(
                    savedLoan.getIdLoan(),
                    savedLoan.getLoanDate(),
                    savedLoan.getDevolutionDate(),
                    savedLoan.getState().name(),
                    savedLoan.getBooksQuantity(),
                    savedLoan.getUser().getCode(),
                    savedLoan.getUser().getName()+" "+savedLoan.getUser().getLastname(),
                    savedLoan.getLibrarian().getCode(),
                    savedLoan.getLibrarian().getName()+" "+savedLoan.getLibrarian().getLastname(),
                    savedLoan.getBooks().stream().map(book -> new BookResponse(
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
            );

        }
        else {
            throw new IllegalArgumentException("Some books are not available for loan.");
        }

    }
    @Transactional
    public LoanResponse updateLoan(Long id, LoanUpdateRequest loanRequest) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", id));
        if(loanRequest.getUserId() != null){
            loan.setUser(userService.getUser(loanRequest.getUserId()));
        }
        if(loanRequest.getLibrarianId() != null){
            loan.setLibrarian(userService.getUser(loanRequest.getLibrarianId()));
        }
        if(loanRequest.getLoanDays() != null){
            loan.setDevolutionDate(loan.getLoanDate().plusDays(loanRequest.getLoanDays()));
        }
        if(loanRequest.getBooksQuantity() != null){
            loan.setBooksQuantity(loanRequest.getBooksQuantity());
        }
        if(loanRequest.getState() != null){
            loan.setState(loanRequest.getState());
        }
        if(loanRequest.getBookIds() != null && !loanRequest.getBookIds().isEmpty()) {
            if (bookService.verifyBooks(loanRequest.getBookIds())) {
                Set<Book> books = bookService.getBooksByIds(loanRequest.getBookIds());
                loan.setBooks(books);
            } else {
                throw new IllegalArgumentException("Some books are not available for loan.");
            }
        }
        Loan updatedLoan = loanRepository.save(loan);
        return new LoanResponse(
                updatedLoan.getIdLoan(),
                updatedLoan.getLoanDate(),
                updatedLoan.getDevolutionDate(),
                updatedLoan.getState().name(),
                updatedLoan.getBooksQuantity(),
                updatedLoan.getUser().getCode(),
                updatedLoan.getUser().getName()+" "+updatedLoan.getUser().getLastname(),
                updatedLoan.getLibrarian().getCode(),
                updatedLoan.getLibrarian().getName()+" "+updatedLoan.getLibrarian().getLastname(),
                updatedLoan.getBooks().stream().map(book -> new BookResponse(
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
        );
    }
    @Transactional
    public LoanResponse updateState(Long id, String state) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", id));
        LoanState loanState;
        try {
            loanState = LoanState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid loan state: " + state);
        }
        loan.setState(loanState);
        Loan updatedLoan = loanRepository.save(loan);
        return new LoanResponse(
                updatedLoan.getIdLoan(),
                updatedLoan.getLoanDate(),
                updatedLoan.getDevolutionDate(),
                updatedLoan.getState().name(),
                updatedLoan.getBooksQuantity(),
                updatedLoan.getUser().getCode(),
                updatedLoan.getUser().getName()+" "+updatedLoan.getUser().getLastname(),
                updatedLoan.getLibrarian().getCode(),
                updatedLoan.getLibrarian().getName()+" "+updatedLoan.getLibrarian().getLastname(),
                updatedLoan.getBooks().stream().map(book -> new BookResponse(
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
        );
    }
    @Transactional
    public void deleteLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", id));
        loanRepository.delete(loan);
    }

}
