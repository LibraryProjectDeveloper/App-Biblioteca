package com.WebBiblioteca.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @Enumerated(value = EnumType.STRING)
    private Role nameRol;

    @Column(name = "descripcion", columnDefinition = "VARCHAR(30)")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 30, message = "El nombre debe tener entre 2 y 30 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "La descripcion solo puede contener letras y caracteres válidos")
    private String descriptionRol;

    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL)
    List<User> users = new ArrayList<>();
}
