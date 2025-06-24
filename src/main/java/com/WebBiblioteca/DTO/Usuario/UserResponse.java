package com.WebBiblioteca.DTO.Usuario;

import com.WebBiblioteca.Model.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserResponse {
    private Long idUsuario;
    private String name;
    private String lastname;
    private String email;
    private String phone;
    private String address;
    private String DNI;
    private String password;
    private Boolean state;
    private LocalDateTime dateRegistered;
    private String roleName;
}
