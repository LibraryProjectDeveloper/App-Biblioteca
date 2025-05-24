package com.WebBiblioteca.Controller;

import com.WebBiblioteca.DTO.Autor.AuthorRequest;
import com.WebBiblioteca.Service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/author")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAuthors(){
        return ResponseEntity.ok(authorService.getAllAuthors());
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateAuthor(@Valid @RequestBody AuthorRequest author){
        return ResponseEntity.ok(authorService.updateAuthor(author));
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updatePartialAuthor(@RequestBody AuthorRequest author){
        return ResponseEntity.ok(authorService.updateAuthor(author));
    }
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAuthor(@RequestParam Long id){
        authorService.deleteAuthor(id);
        return ResponseEntity.ok("Autor eliminado correctamente");
    }

}
