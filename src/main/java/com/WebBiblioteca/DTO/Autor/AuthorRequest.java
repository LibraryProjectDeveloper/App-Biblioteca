package com.WebBiblioteca.DTO.Autor;

import com.WebBiblioteca.Model.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AuthorRequest {
    private Long idAuthor;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 20, message = "El nombre debe tener entre 2 y 20 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El nombre solo puede contener letras y caracteres válidos")
    private String names;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 20, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El apellido solo puede contener letras y caracteres válidos")
    private String lastname;

    @NotBlank(message = "La nacionalidad es obligatorio")
    @Size(min = 2, max = 20, message = "La nacionalidad debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "La nacionalidad solo puede contener letras y caracteres válidos")
    private String nationality;

    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private Date birthdate;

    @NotNull
    private Gender gender;
}
