package com.WebBiblioteca.Controller;

import com.WebBiblioteca.DTO.Book.BookReportRequest;
import com.WebBiblioteca.DTO.Book.BookRequest;
import com.WebBiblioteca.DTO.Book.BookResponse;
import com.WebBiblioteca.DTO.Book.CountBookByCategory;
import com.WebBiblioteca.Model.BookState;
import com.WebBiblioteca.Model.Category;
import com.WebBiblioteca.Service.BookService;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> getAllBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(bookService.getBookList(page,size));
    }
    @GetMapping("/state/{value}")
    public ResponseEntity<?> getAllActiveBooks(@PathVariable BookState value,
                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.getBookList(value,page,size));
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<?> getBooksByPublicationYear(@PathVariable Integer year,
                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.getBooksByPublicationYear(year,page,size));
    }

    @GetMapping("/book-info/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(bookService.getAllCategories());
    }

    @GetMapping({"/categoria/{category}","/book-info/category/{category}"})
    public ResponseEntity<?> getBooksByCategory(@PathVariable Category category, @RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.getBooksByCategory(category, page, size));
    }
    @GetMapping("/book-info/searchTitle")
    public ResponseEntity<?> getBooksByTitle(@RequestParam(required = false) String title) {
        List<BookResponse> bookList = bookService.getBooksByTitle(title);

        if (!bookList.isEmpty()) {
            return ResponseEntity.ok(bookList);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/book-info/available")
    public ResponseEntity<?> getAvailableBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    )
    {
        return ResponseEntity.ok(bookService.booksActiveAndStock(page, size));
    }

    @GetMapping("/searchCategory")
    public ResponseEntity<?> getBooksByCategoryName(@RequestParam(required = false) String category,
                                                    @RequestParam(value = "page",defaultValue = "0") int page,
                                                    @RequestParam(value="size",defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.getBooksByCategoryName(category, page, size));
    }

    @GetMapping("/countBooksLoanedByCategory")
    public ResponseEntity<?> countBooksLoanedByCategory() {
        List<CountBookByCategory> bookList = bookService.countBooksLoanedByCategory();
        if (!bookList.isEmpty()) {
            return ResponseEntity.ok(bookList);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.singletonMap("message", "No hay libros prestados por categoria"));
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

    @GetMapping("/book-info/searchAuthor")
    public ResponseEntity<?> getBooksByAuthor(@RequestParam(required = false) String authorName) {
        List<BookResponse> bookList = bookService.getBooksByAuthor(authorName);
        System.out.println("Length de la lista: " + bookList.size());
        if (!bookList.isEmpty()) {
            return ResponseEntity.ok(bookList);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.singletonMap("message", bookList));
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
