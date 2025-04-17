package com.WebBiblioteca.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter
public class User {
    private Long code;
    private String name;
    private String lastname;
    private String email;
    private String phone;
    private String address;
    private String DNI;
    private String password;
    private Boolean state;
    private LocalDateTime dateRegistered;
    private Role role;
}

