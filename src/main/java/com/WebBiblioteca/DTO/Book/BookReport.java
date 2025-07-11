package com.WebBiblioteca.DTO.Book;

import com.WebBiblioteca.Model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Getter @AllArgsConstructor @NoArgsConstructor
public class BookReport {
    private Long codeBook;
    private String title;
    private String isbn;
    private LocalDate publicationDate;
    private String publisher;
    private Category category;
    private int quantity;

}
