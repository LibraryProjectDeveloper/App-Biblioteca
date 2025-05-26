package com.WebBiblioteca.DTO.Book;

import com.WebBiblioteca.Model.Category;
import com.WebBiblioteca.Model.BookState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
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
}
