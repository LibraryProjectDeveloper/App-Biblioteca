package com.WebBiblioteca.Controller;

import com.WebBiblioteca.DTO.Usuario.UserRequest;
import com.WebBiblioteca.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @GetMapping("/actives")
    public ResponseEntity<?> getAllUsersActives() {
        return ResponseEntity.ok(userService.getAllUsersByState(true));
    }
    @GetMapping("/inactives")
    public ResponseEntity<?> getAllUsersInactives() {
        return ResponseEntity.ok(userService.getAllUsersByState(false));
    }
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserRequest user) {
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado correctamente");
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserRequest user){
        userService.updateUser(user);
        return ResponseEntity.ok("Usuario actualizado correctamente");
    }
    @PatchMapping("/update")
    public ResponseEntity<?> updatePartialUser( @RequestBody UserRequest partialUser) {
        userService.updateUser(partialUser);
        return ResponseEntity.ok("Usuario actualizado correctamente");
    }
}
