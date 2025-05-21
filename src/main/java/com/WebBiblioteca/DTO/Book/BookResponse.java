package com.WebBiblioteca.DTO.Book;

import com.WebBiblioteca.Model.Categoria;
import com.WebBiblioteca.Model.EstadoBook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private Categoria categoria;

    private Integer stockTotal;

    private EstadoBook estado;
}
