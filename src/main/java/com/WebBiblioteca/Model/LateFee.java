package com.WebBiblioteca.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "multa")
public class LateFee {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id_multa")
    private Long idLateFee;

    @Column(columnDefinition = "DECIMAL(10,2)",name = "monto")
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor o igual a 0.01")
    private double amount;

    @Column(columnDefinition = "VARCHAR(100)",name = "motivo")
    @NotBlank(message = "El motivo es obligatorio")
    @Size(min = 2, max = 100, message = "El motivo debe tener entre 2 y 100 caracteres")
    private String reason;

    @Column(columnDefinition = "DATE",name = "fecha_multa")
    @NotNull(message = "La fecha de la multa es obligatoria")
    @PastOrPresent(message = "La fecha de la multa debe ser en el pasado o presente")
    private LocalDateTime lateFeeDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private LateFeeState state;

    @ManyToOne
    @JoinColumn(name = "codigo_prestamo")
    private Loan loan;
}
