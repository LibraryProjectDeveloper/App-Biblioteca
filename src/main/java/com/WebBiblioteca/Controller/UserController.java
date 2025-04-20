package com.WebBiblioteca.Controller;

import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Service.UserService;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.ok(userService.addUser(user));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User user,@PathVariable Long id){
        User updatedUser = userService.updateUser(user,id);
        if(updatedUser != null){
            return ResponseEntity.ok(updatedUser);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @PatchMapping("/updatePartial/{id}")
    public ResponseEntity<?> updatePartialUser(@PathVariable Long id, @RequestBody User partialUser) {
        User updatedUser = userService.updatePartialUser(id, partialUser);
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
            return ResponseEntity.ok(user);
        }else{
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
    }
}
