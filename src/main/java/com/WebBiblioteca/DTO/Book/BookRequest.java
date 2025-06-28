package com.WebBiblioteca.DTO.Book;

import com.WebBiblioteca.DTO.Autor.AuthorRequest;
import com.WebBiblioteca.Model.Category;
import com.WebBiblioteca.Model.BookState;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class BookRequest {
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 2, max = 200, message = "El título debe tener entre 2 y 90 caracteres")
    private String title;

    @NotNull(message = "El isbn no puede ser nulo")
    @NotBlank
    private String isbn;

    @NotNull(message = "La fecha de publicación es obligatoria")
    @PastOrPresent(message = "La fecha de publicación no puede ser futura")
    private LocalDate publicationDate;

    @NotBlank(message = "La editorial es obligatoria")
    @Size(max = 30, message = "La editorial no puede exceder los 30 caracteres")
    private String publisher;

    @NotNull
    private Category category;

    @NotNull(message = "El stock total es obligatorio")
    @Min(value = 0, message = "El stock total no puede ser negativo")
    @Max(value = 10000, message = "El stock total no puede exceder 10,000")
    private Integer stockTotal;

    @NotNull
    private BookState state;

    private List<AuthorRequest> authors;
}
