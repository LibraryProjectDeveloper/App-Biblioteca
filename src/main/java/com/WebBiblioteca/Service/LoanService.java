package com.WebBiblioteca.Service;

import com.WebBiblioteca.DTO.Autor.AuthorResponse;
import com.WebBiblioteca.DTO.Book.BookResponse;
import com.WebBiblioteca.DTO.Loan.LoanRequest;
import com.WebBiblioteca.DTO.Loan.LoanResponse;
import com.WebBiblioteca.DTO.Loan.LoanUpdateRequest;
import com.WebBiblioteca.Exception.ResourceNotFoundException;
import com.WebBiblioteca.Model.*;
import com.WebBiblioteca.Repository.LoanRepository;
import org.springframework.data.domain.Sort;
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
        Sort sort = Sort.by(Sort.Direction.DESC, "loanDate");
        return loanRepository.findAll(sort).stream().map(this::mapToLoanResponse).toList();
    }

    public List<LoanResponse> getLoansByState(LoanState state){
        return loanRepository.findByState(state).stream().map(this::mapToLoanResponse).toList();
    }
    public List<LoanResponse> getLoansByUserId(Long userId) {
        return loanRepository.findByUserCode(userId).stream().map(this::mapToLoanResponse).toList();
    }

    public List<LoanResponse> getLoansByUserDni(String dni, Role role) {
        return loanRepository.findByUserDni(dni,role).stream().map(this::mapToLoanResponse).toList();
    }

    public List<LoanResponse> getLoansByBookTitleOrAuthorNameOrAuthorLastName(Long idUser,String searchTerm) {
        return loanRepository.findByBookTitleOrAuthorNameOrAuthorLastName(idUser,searchTerm).stream().map(this::mapToLoanResponse).toList();
    }

    public List<LoanResponse> getLoanStateByUserCode(LoanState state, Long userCode) {
        return loanRepository.findByStateAndUserCode(state, userCode).stream().map(this::mapToLoanResponse).toList();
    }

    public LoanResponse getLoanById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", id));
        return mapToLoanResponse(loan);
    }
    @Transactional
    public LoanResponse saveLoan(LoanRequest loanRequest,Long idLibrarian) {
        User user = userService.getUser(loanRequest.getUserId());
        User librarian = userService.getUser(idLibrarian);
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
            return mapToLoanResponse(savedLoan);
        }
        else {
            throw new IllegalArgumentException("Some books are not available for loan.");
        }

    }
    @Transactional
    public LoanResponse updateLoan(Long id,Long idLibrarian ,LoanUpdateRequest loanRequest) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", id));
        if(loanRequest.getUserId() != null){
            loan.setUser(userService.getUser(loanRequest.getUserId()));
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
        return mapToLoanResponse(updatedLoan);
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
        return mapToLoanResponse(updatedLoan);
    }

    public LoanResponse finishedLoan(Long id){
        Loan loan = loanRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loan","id",id));
        loan.setState(LoanState.DEVUELTO);
        loan.getBooks().forEach(book -> book.setStockTotal(book.getStockTotal()+1));
        Loan loanUpdate = loanRepository.save(loan);
        return mapToLoanResponse(loanUpdate);
    }
    @Transactional
    public void deleteLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", id));
        loan.setState(LoanState.CANCELADO);
        loan.getBooks().forEach(book -> book.setStockTotal(book.getStockTotal()+1));
        loanRepository.save(loan);
    }

    //Metodos para mapear las entidades a DTOS
    private LoanResponse mapToLoanResponse(Loan loan){
        return new LoanResponse(
                loan.getIdLoan(),
                loan.getLoanDate(),
                loan.getDevolutionDate(),
                loan.getState().name(),
                loan.getBooksQuantity(),
                loan.getUser().getCode(),
                loan.getUser().getName()+" "+loan.getUser().getLastname(),
                loan.getLibrarian().getCode(),
                loan.getLibrarian().getName()+" "+loan.getLibrarian().getLastname(),
                mapToBookResponses(loan.getBooks())
        );
    }

    private List<BookResponse> mapToBookResponses(Set<Book> book){
        return book.stream().map(this::mapToBookResponse).toList();
    }

    private BookResponse mapToBookResponse(Book book){
        return new BookResponse(
                book.getCodeBook(),
                book.getTitle(),
                book.getIsbn(),
                book.getPublicationDate(),
                book.getPublisher(),
                book.getCategory(),
                book.getStockTotal(),
                book.getEstado(),
                mapToAuthorResponses(book.getAutores())
        );
    }


    private AuthorResponse mapToAuthorResponse(Author author) {
        return new AuthorResponse(
                author.getIdAuthor(),
                author.getNames(),
                author.getLastname(),
                author.getNationality(),
                author.getBirthdate(),
                author.getGender()
        );
    }

    private List<AuthorResponse> mapToAuthorResponses(Set<Author> authors) {
        return authors.stream().map(this::mapToAuthorResponse).toList();
    }
}
