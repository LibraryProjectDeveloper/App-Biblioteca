package com.WebBiblioteca.Controller;

import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/all")
    public HashMap<Long,User> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        return ResponseEntity.ok( userService.addUser(user));
    }
}
