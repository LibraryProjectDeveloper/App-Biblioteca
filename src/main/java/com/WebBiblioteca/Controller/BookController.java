package com.WebBiblioteca.Controller;

import com.WebBiblioteca.BookDTO;
import com.WebBiblioteca.Model.Book;
import com.WebBiblioteca.Service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    @GetMapping("/")
    public List<Book> getAllBooks() {
        return bookService.getBookList();
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
    public ResponseEntity<?> addBook(@Valid @RequestBody BookDTO book){
        Book bookAdded = bookService.addBook(book);
        if (bookAdded != null) {
            return ResponseEntity.ok(bookAdded);
        } else {
            return ResponseEntity.badRequest().body("Error adding book");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updatePartialBook(@PathVariable Long id, @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    /*

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        boolean isDeleted = bookService.deleteBook(id);
        if (isDeleted) {
            return ResponseEntity.ok("Book deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

     */
}
