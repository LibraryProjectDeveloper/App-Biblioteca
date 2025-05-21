package com.WebBiblioteca.Controller;

import com.WebBiblioteca.DTO.Book.BookRequest;
import com.WebBiblioteca.DTO.Book.BookResponse;
import com.WebBiblioteca.Model.Book;
import com.WebBiblioteca.Model.EstadoBook;
import com.WebBiblioteca.Service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/actives")
    public ResponseEntity<?> getAllBooks() {
        try {
            List<BookResponse> bookList = bookService.getBookList(EstadoBook.ACTIVO);
            return ResponseEntity.ok(bookList);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }

    @GetMapping("/inactives")
    public ResponseEntity<?> getAllInactivesBooks() {
        try {
            List<BookResponse> bookList = bookService.getBookList(EstadoBook.INACTIVO);
            return ResponseEntity.ok(bookList);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBook(@Valid @RequestBody BookRequest book){
        Book bookAdded = bookService.addBook(book);
        if (bookAdded != null) {
            return ResponseEntity.ok(bookAdded);
        } else {
            return ResponseEntity.badRequest().body("Error adding book");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest book) {
        Book updatedBook = bookService.updateBook(id, book);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updatePartialBook(@PathVariable Long id, @RequestBody BookRequest book) {
        Book updatedBook = bookService.updateBook(id, book);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
