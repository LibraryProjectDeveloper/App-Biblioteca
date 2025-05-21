package com.WebBiblioteca.Controller;

import com.WebBiblioteca.DTO.Autor.AutorRequest;
import com.WebBiblioteca.DTO.Autor.AutorResponse;
import com.WebBiblioteca.Model.Autor;
import com.WebBiblioteca.Service.AutorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autor")
public class AutorController {
    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAutores(){
        try {
            return ResponseEntity.ok(autorService.getAllAuthors());
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAutor(@Valid @RequestBody AutorRequest autor){
        try {
            return ResponseEntity.ok(autorService.updateAutor(autor));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> patchAutor(@RequestBody AutorRequest autor){
        try {
            return ResponseEntity.ok(autorService.updateAutor(autor));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
