package com.WebBiblioteca.Controller;

import com.WebBiblioteca.DTO.Usuario.UserRequest;
import com.WebBiblioteca.DTO.Usuario.UserResponse;
import com.WebBiblioteca.Exception.ResourceNotFoundException;
import com.WebBiblioteca.Model.Role;
import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;


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
    public ResponseEntity<?> getAllUsersActives(@RequestParam(required = false) int page,
                                                @RequestParam(required = false) int size) {
        return ResponseEntity.ok(userService.getAllUsersByState(true, page, size));
    }
    @GetMapping("/inactives")
    public ResponseEntity<?> getAllUsersInactives(@RequestParam(value = "page",defaultValue = "0") int page,
                                                  @RequestParam(value = "size",defaultValue = "10") int size)
    {
        return ResponseEntity.ok(userService.getAllUsersByState(false, page, size));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserResponse(id));
    }

    @GetMapping("/dniRol/{dni}/{rol}")
    public ResponseEntity<?> getUserByDniAndRol(@PathVariable String dni, @PathVariable Role rol) {
        return ResponseEntity.ok(userService.getUserByDniAndRol(dni,rol));
    }
    @GetMapping("/search/{consult}")
    public ResponseEntity<?> searchUser(@PathVariable String consult,
                                        @RequestParam(value = "page",defaultValue = "0") int page,
                                        @RequestParam(value = "size",defaultValue = "10") int size) {
        try {
            return ResponseEntity.ok(userService.getUserByNameOrEmailOrDNI(consult,page,size));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(404).
                    body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @GetMapping("/rol/{idRol}")
    public ResponseEntity<?> getUsersByRol(@PathVariable Long idRol,
                                           @RequestParam(value = "page",defaultValue = "0") int page,
                                           @RequestParam(value = "size",defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.findByRol(idRol,page,size));
    }

    @GetMapping("/existsEmail/{email}")
    public ResponseEntity<?> existsUserByEmail(@PathVariable String email) {
        try {
            UserResponse userExists = userService.getUserByEmail(email);
            if (userExists != null) {
                return ResponseEntity.ok(Collections.singletonMap("exists", true));
            }
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(404).body(Collections.singletonMap("exists", false));
    }

    @GetMapping("/existsDni/{dni}")
    public ResponseEntity<?> existsUserByDni(@PathVariable String dni) {
        try {
            UserResponse userExists = userService.getUserByDNI(dni);
            if (userExists != null) {
                return ResponseEntity.ok(Collections.singletonMap("exists", true));
            }
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(404).body(Collections.singletonMap("exists", false));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserRequest user) {
        UserResponse userResponse = userService.addUser(user);
        URI location = URI.create("/api/user/" + userResponse.getIdUsuario());
        return ResponseEntity.created(location).body(userResponse);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserRequest user,@PathVariable Long id) {
        return ResponseEntity.ok(userService.updateUser(user,id));
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updatePartialUser( @RequestBody UserRequest partialUser, @PathVariable Long id) {
        return ResponseEntity.ok(userService.updateUser(partialUser,id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
