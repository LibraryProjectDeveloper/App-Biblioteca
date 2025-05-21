package com.WebBiblioteca.Controller;

import com.WebBiblioteca.DTO.Usuario.UsuarioRequest;
import com.WebBiblioteca.DTO.Usuario.UsuarioResponse;
import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody UsuarioRequest user) {
        try {
            userService.addUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado correctamente");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UsuarioRequest user){
        try {
           userService.updateUser(user);
            return ResponseEntity.ok("Usuario actualizado correctamente");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PatchMapping("/update")
    public ResponseEntity<?> updatePartialUser( @RequestBody UsuarioRequest partialUser) {
        try {
            userService.updateUser(partialUser);
            return ResponseEntity.ok("Usuario actualizado correctamente");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    /*

    @PostMapping("/login/{email}/{password}")
    public ResponseEntity<?> login(@PathVariable String email,@PathVariable String password){
        User user = userService.loginUser(email,password);
        if(user != null){
            return ResponseEntity.ok("Login successful");
        }else{
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
    }
     */
}
