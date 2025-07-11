package com.WebBiblioteca.DTO.Book;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Getter @AllArgsConstructor @NoArgsConstructor
public class BookReportRequest {
    @NotNull(message = "the date cannot be null")
    private LocalDate dateStart;
    @NotNull(message = "the date cannot be null")
    private LocalDate dateEnd;
    @NotNull(message = "the category cannot be null")
    private String category;

}
