package com.WebBiblioteca.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Entity
public class Multa {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long idMulta;

    @Column(columnDefinition = "DECIMAL(10,2)")
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor o igual a 0.01")
    private BigDecimal monto;

    @Column(columnDefinition = "VARCHAR(100)")
    @NotBlank(message = "El motivo es obligatorio")
    @Size(min = 2, max = 100, message = "El motivo debe tener entre 2 y 100 caracteres")
    private String motivo;

    @Column(columnDefinition = "DATE")
    @NotNull(message = "La fecha de la multa es obligatoria")
    @PastOrPresent(message = "La fecha de la multa debe ser en el pasado o presente")
    private Date fechaMulta;

    @Enumerated(EnumType.STRING)
    private EstadoMulta estado;

    @ManyToOne
    @JoinColumn(name = "codigo_prestamo")
    private Prestamo prestamo;
}
