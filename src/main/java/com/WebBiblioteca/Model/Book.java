package com.WebBiblioteca.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "libro")
public class Book {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id_libro")
    private Long codeBook;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 2, max = 255, message = "El título debe tener entre 2 y 255 caracteres")
    @Column(name = "titulo_libro", columnDefinition = "VARCHAR(255)")
    private String title;

    @NotNull(message = "El isbn no puede ser nulo")
    @NotBlank
    @Column(name = "isbn",unique = true,columnDefinition = "VARCHAR(17)")
    private String isbn;

    @NotNull(message = "La fecha de publicación es obligatoria")
    @PastOrPresent(message = "La fecha de publicación no puede ser futura")
    @Column(name = "fecha_publicacion", columnDefinition = "DATE")
    private LocalDate publicationDate;

    @NotBlank(message = "La editorial es obligatoria")
    @Size(max = 30, message = "La editorial no puede exceder los 30 caracteres")
    @Column(name = "editorial", columnDefinition = "VARCHAR(30)")
    private String publisher;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @NotNull(message = "El stock total es obligatorio")
    @Min(value = 0, message = "El stock total no puede ser negativo")
    @Max(value = 10000, message = "El stock total no puede exceder 10,000")
    @Column(name = "stock_total", columnDefinition = "INTEGER")
    private Integer stockTotal;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BookState estado;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ReserveBook> listReservasBook = new ArrayList<>();

    @ManyToMany(mappedBy = "libros")
    private Set<Prestamo> prestamos = new HashSet<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id"))
    private Set<Author> autores = new HashSet<>();
}
