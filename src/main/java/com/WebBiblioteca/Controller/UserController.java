package com.WebBiblioteca.Controller;

import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/")
    public HashMap<Long,User> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user,  BindingResult result) {
        Map<String,String> errores = new HashMap<>();
        System.out.println(user.getDNI());
        if (result.hasErrors()){
            result.getFieldErrors().forEach(error -> {
                System.out.println("Error in field: " + error.getField() + " - " + error.getDefaultMessage());
                errores.put(error.getField(),error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errores);
        }
        User newuser  = userService.addUser(user);
        if (newuser == null) {
            return ResponseEntity.badRequest().body("User with this email already exists");
        }
        return ResponseEntity.ok(newuser);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user,@PathVariable Long id, BindingResult result){
        User updatedUser = userService.updateUser(user,id);
        if (result.hasErrors()){
            result.getFieldErrors().forEach(error -> {
                System.out.println("Error in field: " + error.getField() + " - " + error.getDefaultMessage());
            });
        }
        if(updatedUser != null){
            return ResponseEntity.ok(updatedUser);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updatePartialUser(@RequestBody User partialUser,@PathVariable Long id) {
        User updatedUser = userService.updateUser(partialUser,id);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean result = userService.deleteUser(id);
        if(result){
            return ResponseEntity.ok("User deleted successfully");
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/login/{email}/{password}")
    public ResponseEntity<?> login(@PathVariable String email,@PathVariable String password){
        User user = userService.loginUser(email,password);
        if(user != null){
            return ResponseEntity.ok("Login successful");
        }else{
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
    }
}
