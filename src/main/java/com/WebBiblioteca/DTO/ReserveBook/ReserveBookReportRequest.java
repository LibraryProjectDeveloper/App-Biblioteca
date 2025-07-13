package com.WebBiblioteca.DTO.ReserveBook;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter @AllArgsConstructor
public class ReserveBookReportRequest {
    //@NotNull(message = "the date cannot be null")
    private LocalDate dateStart;
    //@NotNull(message = "the date cannot be null")
    private LocalDate dateEnd;
}
