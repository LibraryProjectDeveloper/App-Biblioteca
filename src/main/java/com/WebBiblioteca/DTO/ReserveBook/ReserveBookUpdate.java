package com.WebBiblioteca.DTO.ReserveBook;

import com.WebBiblioteca.Model.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @AllArgsConstructor @NoArgsConstructor
public class ReserveBookUpdate {
    private Long id;
    private Long BookId;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate reservationDate;
    private Boolean isActive;
}
