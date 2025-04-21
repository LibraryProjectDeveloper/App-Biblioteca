package com.WebBiblioteca.Service;

import com.WebBiblioteca.Model.Book;
import com.WebBiblioteca.Repository.BookReposity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {
    private final BookReposity bookReposity = new BookReposity();
    private final BookService bookService = new BookService(bookReposity);

    @Test
    void TestAddBook(){
        Book book = new Book(0L,"Los perros hambrientos", LocalDate.now(),"Ciro Alegria","Novela",10);
        Book result = bookService.addBook(book);
        assertEquals("Los perros hambrientos", result.getTitle());
        assertEquals(LocalDate.now(), result.getPublicationDate());
        assertEquals("Ciro Alegria", result.getPublisher());
        assertEquals("Novela", result.getCategory());
        assertEquals(10, result.getStockTotal());
        assertNotNull(result);
    }
    @Test
    void testGetBookList() {
        Book book1 = new Book(null, "Los perros hambrientos", LocalDate.now(), "Ciro Alegria", "Novela", 10);
        Book book2 = new Book(null, "La ciudad y los perros", LocalDate.now(), "Mario Vargas Llosa", "Novela", 5);
        bookService.addBook(book1);
        bookService.addBook(book2);
        Map<Long,Book> books = bookService.getBookList();
        assertEquals(2, books.size());
        assertNotNull(books.get(1L));
        assertNotNull(books.get(2L));
        assertEquals("Los perros hambrientos", books.get(1L).getTitle());
        assertEquals("La ciudad y los perros", books.get(2L).getTitle());
    }

    @Test
    void testAddBookNull() {
        Book result = bookService.addBook(null);
        assertNull(result);
    }

    @Test
    void testUpdateBookSuccess() {
        Book book = new Book(null, "Los perros hambrientos", LocalDate.now(), "Ciro Alegria", "Novela", 10);
        Book savedBook = bookService.addBook(book);
        Book updatedInfo = new Book();
        updatedInfo.setTitle("Los perros hambrientos (Edición especial)");
        updatedInfo.setStockTotal(15);
        Book result = bookService.updateBook(savedBook.getCodeBook(), updatedInfo);
        assertNotNull(result);
        assertEquals("Los perros hambrientos (Edición especial)", result.getTitle());
        assertEquals(15, result.getStockTotal());
        assertEquals("Ciro Alegria", result.getPublisher());
    }

    @Test
    void testUpdateBookBookNotFound() {
        Book updatedInfo = new Book();
        updatedInfo.setTitle("Libro actualizado");
        Book result = bookService.updateBook(999L, updatedInfo);
        assertNull(result);
    }

    @Test
    void testUpdateBookNullFields() {
        Book book = new Book(null, "Los perros hambrientos", LocalDate.now(), "Ciro Alegria", "Novela", 10);
        Book savedBook = bookService.addBook(book);
        Book updatedInfo = new Book();
        Book result = bookService.updateBook(savedBook.getCodeBook(), updatedInfo);
        assertNotNull(result);
        assertEquals("Los perros hambrientos", result.getTitle());
        assertEquals("Ciro Alegria", result.getPublisher());
    }

    @Test
    void testDeleteBookSuccess() {
        Book book = new Book(null, "Los perros hambrientos", LocalDate.now(), "Ciro Alegria", "Novela", 10);
        Book savedBook = bookService.addBook(book);
        boolean result = bookService.deleteBook(savedBook.getCodeBook());
        assertTrue(result);
        assertNull(bookService.getBookList().get(savedBook.getCodeBook()));
    }

    @Test
    void testDeleteBookBookNotFound() {
        boolean result = bookService.deleteBook(999L);
        assertFalse(result);
    }
}
