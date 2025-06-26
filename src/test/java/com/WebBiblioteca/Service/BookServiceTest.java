package com.WebBiblioteca.Service;

import com.WebBiblioteca.Model.Book;
import com.WebBiblioteca.Repository.BookReposity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.WebBiblioteca.DTO.Book.BookRequest;
import com.WebBiblioteca.DTO.Book.BookResponse;
import com.WebBiblioteca.Model.BookState;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.Optional;
import java.util.List;
import static org.mockito.Mockito.*;

public class BookServiceTest {
    @Mock
    private BookReposity bookReposity;
    @InjectMocks
    private BookService bookService;
    @InjectMocks
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBookList() {
        BookState state = BookState.ACTIVO;
        Book book = mock(Book.class);
        when(book.getCodeBook()).thenReturn(1L);
        when(book.getTitle()).thenReturn("Libro 1");
        when(book.getIsbn()).thenReturn("123");
        when(book.getPublicationDate()).thenReturn(null);
        when(book.getPublisher()).thenReturn("Editorial");
        when(book.getCategory()).thenReturn(null);
        when(book.getStockTotal()).thenReturn(5);
        when(book.getEstado()).thenReturn(state);
        when(bookReposity.findByEstado(state)).thenReturn(List.of(book));
        List<BookResponse> result = bookService.getBookList(state);
        assertEquals(1, result.size());
        assertEquals("Libro 1", result.getFirst().getTitle());
    }

    @Test
    void testGetBookById() {
        Book book = mock(Book.class);
        when(bookReposity.findByCodeBook(1L)).thenReturn(Optional.of(book));
        when(book.getTitle()).thenReturn("Libro 1");
        when(book.getIsbn()).thenReturn("123");
        when(book.getPublicationDate()).thenReturn(null);
        when(book.getPublisher()).thenReturn("Editorial");
        when(book.getCategory()).thenReturn(null);
        when(book.getStockTotal()).thenReturn(5);
        when(book.getEstado()).thenReturn(BookState.ACTIVO);
        when(book.getAutores()).thenReturn(Collections.emptySet());
        BookRequest result = bookService.getBookById(1L);
        assertEquals("Libro 1", result.getTitle());
    }

@Test
void testAddBook() {
    BookRequest req = mock(BookRequest.class);
    when(req.getIsbn()).thenReturn("123");
    when(req.getTitle()).thenReturn("Libro Nuevo");
    when(req.getPublisher()).thenReturn("Editorial");
    when(req.getPublicationDate()).thenReturn(null);
    when(req.getCategory()).thenReturn(null);
    when(req.getStockTotal()).thenReturn(10);
    when(req.getAuthor()).thenReturn(Collections.emptyList());
    when(bookReposity.findByIsbn("123")).thenReturn(Optional.empty());

    AuthorService mockAuthorService = mock(AuthorService.class);
    when(mockAuthorService.getAllAuthors()).thenReturn(Collections.emptyList());
    when(mockAuthorService.castAuthorResponseListToAuthor(anyList())).thenReturn(Collections.emptySet());
    BookService bookServiceWithMock = new BookService(bookReposity, mockAuthorService);
    Book book = new Book();
    when(bookReposity.save(any(Book.class))).thenReturn(book);
    BookResponse result = bookServiceWithMock.addBook(req);
    assertNotNull(result);
}

    @Test
    void testUpdateBook() {
        BookRequest req = mock(BookRequest.class);
        Book book = new Book();
        when(bookReposity.findByCodeBook(1L)).thenReturn(Optional.of(book));
        when(bookReposity.save(any(Book.class))).thenReturn(book);
        BookResponse result = bookService.updateBook(1L, req);
        assertNotNull(result);
    }
}
