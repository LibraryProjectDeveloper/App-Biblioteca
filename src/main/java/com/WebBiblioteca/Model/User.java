package com.WebBiblioteca.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuario")
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class User {
        @Id
        @Column(name = "us_codigo")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long code;

        @Column(name = "us_nombres", columnDefinition = "VARCHAR(50)")
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El nombre solo puede contener letras y caracteres válidos")
        private String name;

        @Column(name = "us_apellidos", columnDefinition = "VARCHAR(50)")
        @NotBlank(message = "El apellido es obligatorio")
        @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
        @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El apellido solo puede contener letras y caracteres válidos")
        private String lastname;

        @Column(name = "us_correo", columnDefinition = "VARCHAR(100)", unique = true)
        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "Debe proporcionar un correo electrónico válido",
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        @Size(max = 100, message = "El correo electrónico no puede exceder los 100 caracteres")
        private String email;

        @Column(name = "us_telefono", columnDefinition = "VARCHAR(10)")
        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^[9]\\d{8}$",
                message = "El teléfono debe tener 9 dígitos y comenzar 9")
        private String phone;

        @Column(name = "us_direccion", columnDefinition = "VARCHAR(150)")
        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 200, message = "La dirección no puede exceder los 200 caracteres")
        private String address;

        @Column(name = "us_dni", columnDefinition = "VARCHAR(8)", unique = true)
        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "^[0-9]{8}$",
                message = "El DNI debe tener 8")
        private String DNI;

        @Column(name = "us_contrasena", columnDefinition = "VARCHAR(250)")
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
        private String password;

        @Column(name = "us_estado")
        private Boolean state;

        @Column(name = "us_fecha_registro", columnDefinition = "TIMESTAMP")
        private LocalDateTime dateRegistered;

        @ManyToOne()
        @JoinColumn(name = "rol_id")
        private Rol rol;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
        @JsonManagedReference
        private Set<ReserveBook> listReservasUser = new HashSet<>();

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
        private Set<Prestamo> listPrestamosUser = new HashSet<>();

}

