package com.WebBiblioteca.DTO.Book;

import com.WebBiblioteca.DTO.Autor.AuthorResponse;
import com.WebBiblioteca.Model.Category;
import com.WebBiblioteca.Model.BookState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
public class BookResponse {
    private Long codeBook;
    private String title;

    private String isbn;

    private LocalDate publicationDate;

    private String publisher;

    private Category category;

    private Integer stockTotal;

    private BookState state ;

    private List<AuthorResponse> author;

    public BookResponse(Long codeBook, String title, String isbn, LocalDate publicationDate, String publisher, Category category, Integer stockTotal, BookState state) {
        this.codeBook = codeBook;
        this.title = title;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        this.category = category;
        this.stockTotal = stockTotal;
        this.state = state;
    }

    public BookResponse(Long codeBook, String title, String isbn, LocalDate publicationDate, String publisher, Category category, Integer stockTotal, BookState state, List<AuthorResponse> author) {
        this.codeBook = codeBook;
        this.title = title;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        this.category = category;
        this.stockTotal = stockTotal;
        this.state = state;
        this.author = author;
    }
}
