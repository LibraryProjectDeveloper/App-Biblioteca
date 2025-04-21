package com.WebBiblioteca.Model;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ReserveBook {

    private Long codeReserve; //se le genera el codigo automaticamente
    @FutureOrPresent(message = "La fecha de reserva no puede ser pasada")
    private LocalDate dateReserve;
    private Boolean state; // se asigna true al momento de hacer la reserva

    private LocalTime startTime;
    private LocalTime endTime;
    private User user;
    private Book book;

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationEnd = LocalDateTime.of(dateReserve, endTime);
        return Boolean.TRUE.equals(state) && now.isBefore(reservationEnd);
    }
}

