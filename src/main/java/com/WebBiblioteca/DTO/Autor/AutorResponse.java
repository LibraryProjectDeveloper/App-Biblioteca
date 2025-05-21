package com.WebBiblioteca.DTO.Autor;

import com.WebBiblioteca.Model.Genero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Date;

@AllArgsConstructor @NoArgsConstructor
@Getter
public class AutorResponse {
    private Long idAutor;
    private String nombres;
    private String apellidos;
    private String nacionalidad;
    private Date fechaNacimiento;
    private Genero genero;
}
