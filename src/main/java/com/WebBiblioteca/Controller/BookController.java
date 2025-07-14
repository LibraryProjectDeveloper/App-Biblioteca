package com.WebBiblioteca.Controller;

import com.WebBiblioteca.DTO.Book.BookReportRequest;
import com.WebBiblioteca.DTO.Book.BookRequest;
import com.WebBiblioteca.DTO.Book.BookResponse;
import com.WebBiblioteca.Model.BookState;
import com.WebBiblioteca.Model.Category;
import com.WebBiblioteca.Service.BookService;
import jakarta.validation.Valid;
import org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/LIBRARIAN/books")
public class BookController {
    private final BookService bookService;
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllBooks() {
        List<BookResponse> bookList = bookService.getBookList();
        return ResponseEntity.ok(bookList);
    }
    @GetMapping("/state/{value}")
    public ResponseEntity<?> getAllActiveBooks(@PathVariable BookState value) {
        List<BookResponse> bookList = bookService.getBookList(value);
        return ResponseEntity.ok(bookList);
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<?> getBooksByPublicationYear(@PathVariable Integer year) {
        return ResponseEntity.ok(bookService.getBooksByPublicationYear(year));
    }

    @GetMapping("/book-info/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(bookService.getAllCategories());
    }

    @GetMapping("/categoria/{category}")
    public ResponseEntity<?> getBooksByCategory(@PathVariable Category category) {
        return ResponseEntity.ok(bookService.filterBookCategory(category));
    }
    @GetMapping("/searchTitle")
    public ResponseEntity<?> getBooksByTitle(@RequestParam(required = false) String title) {
        List<BookResponse> bookList = bookService.getBooksByTitle(title);

        if (!bookList.isEmpty()) {
            return ResponseEntity.ok(bookList);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/book-info/available")
    public ResponseEntity<?> getAvailableBooks() {
        try {
            List<BookResponse> bookList = bookService.booksActiveAndStock();

            if (!bookList.isEmpty()) {
                return ResponseEntity.ok(bookList);
            }
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.singletonMap("message", "Libros no disponibles"));
        }

    }

    @GetMapping("/searchCategory")
    public ResponseEntity<?> getBooksByCategoryName(@RequestParam(required = false) String category) {
        List<BookResponse> bookList = bookService.getBooksByCategoryName(category);

        if (!bookList.isEmpty()) {
            return ResponseEntity.ok(bookList);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBook(@Valid @RequestBody BookRequest book){
        BookResponse bookCreated = bookService.addBook(book);
        URI location = URI.create("/api/LIBRARIAN/books/" + bookCreated.getCodeBook());
        return ResponseEntity.created(location).body(bookCreated);
    }
    @PostMapping("/download-report")
    public ResponseEntity<?> generateReport(@Valid @RequestBody BookReportRequest book){
        byte[] file = bookService.createReportExcel(book.getDateStart(),book.getDateEnd(),book.getCategory());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDisposition(ContentDisposition.attachment().filename("Report.xlsx").build());
        return new ResponseEntity<>(file,headers, HttpStatus.OK);
    }
    @GetMapping("/report")
    public ResponseEntity<?> getBookDataReport(
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd,
            @RequestParam("category") String category
    ){
        return ResponseEntity.ok(bookService.getPopularBooks(dateStart,dateEnd,category));

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
