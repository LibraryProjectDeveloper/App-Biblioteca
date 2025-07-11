package com.WebBiblioteca.DTO.Book;

import com.WebBiblioteca.Model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Getter @AllArgsConstructor @NoArgsConstructor
public class BookReportDto {
    private Long codeBook;
    private String isbn;
    private String title;
    private Category category;
    private LocalDate publicationDate;
    private String publisher;
    private int quantity;
}
