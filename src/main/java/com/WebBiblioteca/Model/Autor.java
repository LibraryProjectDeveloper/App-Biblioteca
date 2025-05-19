package com.WebBiblioteca.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Autor {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long idAutor;

    @Column(columnDefinition = "VARCHAR(20)")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 20, message = "El nombre debe tener entre 2 y 20 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El nombre solo puede contener letras y caracteres válidos")
    private String nombres;

    @Column(columnDefinition = "VARCHAR(20)")
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 20, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El apellido solo puede contener letras y caracteres válidos")
    private String apellidos;

    @Column(columnDefinition = "VARCHAR(20)")
    @NotBlank(message = "La nacionalidad es obligatorio")
    @Size(min = 2, max = 20, message = "La nacionalidad debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "La nacionalidad solo puede contener letras y caracteres válidos")
    private String nacionalidad;

    @Column(columnDefinition = "DATE")
    @NotBlank(message = "La fecha de nacimiento es obligatorio")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private Date fechaNacimiento;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    @ManyToMany(mappedBy = "autores")
    @JsonBackReference
    private Set<Book> libros = new HashSet<>();
}
