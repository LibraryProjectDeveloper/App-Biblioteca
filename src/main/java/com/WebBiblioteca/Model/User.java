package com.WebBiblioteca.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter
public class User {
        private Long code;

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El nombre solo puede contener letras y caracteres válidos")
        private String name;

        @NotBlank(message = "El apellido es obligatorio")
        @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
        @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El apellido solo puede contener letras y caracteres válidos")
        private String lastname;

        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "Debe proporcionar un correo electrónico válido",
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        @Size(max = 100, message = "El correo electrónico no puede exceder los 100 caracteres")
        private String email;

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^[9]\\d{8}$",
                message = "El teléfono debe tener 9 dígitos y comenzar 9")
        private String phone;

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 200, message = "La dirección no puede exceder los 200 caracteres")
        private String address;

        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "^\\d{8}$",
                message = "El DNI debe tener 8")
        private String DNI;

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
        //@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
           //     message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial")
        private String password;

       // @NotNull(message = "El estado es obligatorio") se agrega al agregar el usuario en el repositorio
        private Boolean state;

       // @NotNull(message = "La fecha de registro es obligatoria") la fecha puede ser nula porque al agregar un usuario
        // no se le asigna una fecha de registro, se asigna al momento de agregarlo en el repositorio
       // @PastOrPresent(message = "La fecha de registro no puede ser futura")
        private LocalDateTime dateRegistered;

        @NotNull(message = "El rol es obligatorio")
        private Role role;
}

