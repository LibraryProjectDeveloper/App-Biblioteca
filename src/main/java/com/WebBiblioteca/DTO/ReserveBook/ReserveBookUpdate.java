package com.WebBiblioteca.DTO.ReserveBook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @AllArgsConstructor @NoArgsConstructor
public class ReserveBookUpdate {
    private Long id;
    private Long bookId;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate reservationDate;
    private Boolean isActive;
}
