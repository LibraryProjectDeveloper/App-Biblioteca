package com.WebBiblioteca.DTO.ReserveBook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @NoArgsConstructor @AllArgsConstructor
public class ReserveBookReportDto {
    private LocalDate dateReserve;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String titleBook;
    private String userName;
    private String librarianName;
}
