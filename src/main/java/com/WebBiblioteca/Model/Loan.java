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
@Table(name = "prestamo")
public class Loan {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id_prestamo")
    private Long idLoan;

    @Column(columnDefinition = "TIMESTAMP",name="fecha_prestamo")
    @NotNull(message = "La fecha de préstamo es obligatoria")
    @PastOrPresent(message = "La fecha de préstamo no puede ser futura")
    private LocalDateTime loanDate;

    @Column(columnDefinition = "DATE",name="fecha_devolucion")
    @NotNull(message = "La fecha de devolución es obligatoria")
    private LocalDateTime devolutionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private LoanState state;

    @Positive(message = "La cantidad de libros debe ser positiva")
    @Column(name = "cantidad_libros")
    private Integer booksQuantity;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "librarian_id")
    private User librarian;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    List<LateFee> lateFees = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "prestamo_libro",
            joinColumns = @JoinColumn(name = "prestamo_id"),
            inverseJoinColumns = @JoinColumn(name = "libro_id"))
    private Set<Book> books = new HashSet<>();
}
