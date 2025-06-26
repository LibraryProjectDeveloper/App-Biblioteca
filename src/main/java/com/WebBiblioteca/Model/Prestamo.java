package com.WebBiblioteca.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor @NoArgsConstructor
@Getter
@Setter
@Entity
public class Prestamo {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long idPrestamo;

    @Column(columnDefinition = "TIMESTAMP")
    @NotNull(message = "La fecha de préstamo es obligatoria")
    @PastOrPresent(message = "La fecha de préstamo no puede ser futura")
    private LocalDateTime fechaPrestamo;

    @Column(columnDefinition = "DATE")
    @NotNull(message = "La fecha de devolución es obligatoria")
    @Future(message = "La fecha de devolución debe ser una fecha futura")
    private Date fechaDevolucion;

    @Enumerated(EnumType.STRING)
    private EstadoPrestamo estado;

    @Positive(message = "La cantidad de libros debe ser positiva")
    private Integer cantidadLibros;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "bibliotecario_id")
    private User librarian;

    @OneToMany(mappedBy = "prestamo", cascade = CascadeType.ALL)
    List<Multa> multas = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "prestamo_libro",
            joinColumns = @JoinColumn(name = "prestamo_id"),
            inverseJoinColumns = @JoinColumn(name = "libro_id"))
    private Set<Book> libros = new HashSet<>();
}
