package com.WebBiblioteca.Controller;

import com.WebBiblioteca.DTO.Autor.AuthorRequest;
import com.WebBiblioteca.DTO.Autor.AuthorResponse;
import com.WebBiblioteca.Service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public ResponseEntity<?> addAuthor(@Valid @RequestBody AuthorRequest author){
        AuthorResponse authorResponse = authorService.addAuthor(author);
        URI location = URI.create("/api/author/" + authorResponse.getIdAuthor());
        return ResponseEntity.created(location).body(authorResponse);
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public ResponseEntity<?> updateAuthor(@Valid @RequestBody AuthorRequest author,@PathVariable Long id){
        return ResponseEntity.ok(authorService.updateAuthor(author,id));
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public ResponseEntity<?> updatePartialAuthor(@RequestBody AuthorRequest author, @PathVariable Long id){
        return ResponseEntity.ok(authorService.updateAuthor(author,id));
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAuthor(@PathVariable Long id){
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

}
