package com.WebBiblioteca.Controller;

import com.WebBiblioteca.DTO.Book.BookRequest;
import com.WebBiblioteca.DTO.Book.BookResponse;
import com.WebBiblioteca.Model.Book;
import com.WebBiblioteca.Model.BookState;
import com.WebBiblioteca.Service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/LIBRARIAN/books")
public class BookController {
    private final BookService bookService;
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/actives")
    public ResponseEntity<?> getAllBooks() {
        List<BookResponse> bookList = bookService.getBookList(BookState.ACTIVO);
        return ResponseEntity.ok(bookList);
    }

    @GetMapping("/inactives")
    public ResponseEntity<?> getAllInactivesBooks() {
        List<BookResponse> bookList = bookService.getBookList(BookState.INACTIVO);
        return ResponseEntity.ok(bookList);
    }
    @GetMapping("/book-info/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBook(@Valid @RequestBody BookRequest book){
        BookResponse bookCreated = bookService.addBook(book);
        URI location = URI.create("/api/LIBRARIAN/books/" + bookCreated.getCodeBook());
        return ResponseEntity.created(location).body(bookCreated);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest book) {
        return ResponseEntity.ok(bookService.updateBook(id, book));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updatePartialBook(@PathVariable Long id, @RequestBody BookRequest book) {
        return ResponseEntity.ok(bookService.updateBook(id, book));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
