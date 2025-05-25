package com.WebBiblioteca.DTO.Autor;

import com.WebBiblioteca.Model.Genero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Date;

@AllArgsConstructor @NoArgsConstructor
@Getter
public class AuthorResponse {
    private Long idAuthor;
    private String names;
    private String lastname;
    private String nationality;
    private Date birthdate;
    private Genero gender;
}
