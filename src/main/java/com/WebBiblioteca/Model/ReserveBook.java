package com.WebBiblioteca.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
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
@Entity
@Table(name = "reserva")
public class ReserveBook {
    @Id
    @Column(name = "id_reserva")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codeReserve; //se le genera el codigo automaticamente

    @Column(name = "fecha_reserva", columnDefinition = "DATE")
    @NotNull(message = "La fecha de reserva es obligatoria")
    @FutureOrPresent(message = "La fecha de reserva no puede ser pasada")
    private LocalDate dateReserve;

    @Column(name = "estado_reserva", columnDefinition = "BOOLEAN")
    private Boolean state; // se asigna true al momento de hacer la reserva

    @Column(name = "hora_inicio", columnDefinition = "TIME")
    private LocalTime startTime;

    @Column(name = "hora_fin", columnDefinition = "TIME")
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "librarian_id")
    private User librarian;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonBackReference
    private Book book;

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationEnd = LocalDateTime.of(dateReserve, endTime);
        return Boolean.TRUE.equals(state) && now.isBefore(reservationEnd);
    }
}

