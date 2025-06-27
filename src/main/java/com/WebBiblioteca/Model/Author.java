package com.WebBiblioteca.Model;

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
@Table(name = "autor")
public class Author {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id_autor")
    private Long idAuthor;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 20, message = "El nombre debe tener entre 2 y 20 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El nombre solo puede contener letras y caracteres válidos")
    @Column(name = "nombres")
    private String names;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 20, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El apellido solo puede contener letras y caracteres válidos")
    @Column(name = "apellidos")
    private String lastname;

    @NotBlank(message = "La nacionalidad es obligatorio")
    @Size(min = 2, max = 20, message = "La nacionalidad debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "La nacionalidad solo puede contener letras y caracteres válidos")
    @Column(name = "nacionalidad")
    private String nationality;


    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    @Column(name = "fecha_nacimiento")
    private Date birthdate;

    @Enumerated(EnumType.STRING)
    @Column(name="genero")
    private Gender gender;

    @ManyToMany(mappedBy = "autores")
    private Set<Book> books = new HashSet<>();
}
