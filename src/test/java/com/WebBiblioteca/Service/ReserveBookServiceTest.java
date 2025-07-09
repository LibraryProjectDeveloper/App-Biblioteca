package com.WebBiblioteca.Service;

import com.WebBiblioteca.DTO.ReserveBook.ReserveBookRequest;
import com.WebBiblioteca.DTO.ReserveBook.ReserveBookResponse;
import com.WebBiblioteca.DTO.ReserveBook.ReserveBookUpdate;
import com.WebBiblioteca.Exception.ResourceNotFoundException;
import com.WebBiblioteca.Model.Book;
import com.WebBiblioteca.Model.ReserveBook;
import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Repository.ReserveBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReserveBookServiceTest {
    @Mock
    private ReserveBookRepository reserveBookRepository;
    @Mock
    private BookService bookService;
    @Mock
    private UserService userService;
    @InjectMocks
    private ReserveBookService reserveBookService;

    private ReserveBook reserveBook;
    private User user;
    private User librarian;
    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setCode(1L);
        user.setName("Juan");
        user.setLastname("Perez");
        librarian = new User();
        librarian.setCode(2L);
        librarian.setName("Maria");
        librarian.setLastname("Gomez");
        book = new Book();
        book.setCodeBook(1L);
        book.setTitle("Libro Test");
        reserveBook = new ReserveBook();
        reserveBook.setCodeReserve(1L);
        reserveBook.setBook(book);
        reserveBook.setUser(user);
        reserveBook.setLibrarian(librarian);
        reserveBook.setState(true);
        reserveBook.setDateReserve(LocalDate.now());
        reserveBook.setStartTime(LocalTime.of(10,0));
        reserveBook.setEndTime(LocalTime.of(12,0));
    }

    @Test
    void testGetReservationList() {
        when(reserveBookRepository.findAll()).thenReturn(Arrays.asList(reserveBook));
        List<ReserveBookResponse> result = reserveBookService.getReservationList();
        assertEquals(1, result.size());
        assertEquals("Libro Test", result.get(0).getBookTitle());
    }

    @Test
    void testGetReservationActives() {
        when(reserveBookRepository.findByState(true)).thenReturn(Arrays.asList(reserveBook));
        List<ReserveBookResponse> result = reserveBookService.getReservationActives();
        assertEquals(1, result.size());
        assertTrue(result.getFirst().isActive());
    }

    @Test
    void testGerReservationInactive() {
        reserveBook.setState(false);
        when(reserveBookRepository.findByState(false)).thenReturn(Arrays.asList(reserveBook));
        List<ReserveBookResponse> result = reserveBookService.gerReservationInactive();
        assertEquals(1, result.size());
        assertFalse(result.getFirst().isActive());
    }

    @Test
    void testGetReservationById() {
        when(reserveBookRepository.findById(1L)).thenReturn(Optional.of(reserveBook));
        ReserveBookResponse response = reserveBookService.getReservationById(1L);
        assertEquals("Libro Test", response.getBookTitle());
        assertEquals("Juan Perez", response.getUserName());
    }

    @Test
    void testGetReservationById_NotFound() {
        when(reserveBookRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reserveBookService.getReservationById(2L));
    }

    @Test
    void testGetReservationByUser() {
        when(reserveBookRepository.findByUserId(1L)).thenReturn(Arrays.asList(reserveBook));
        List<ReserveBookResponse> result = reserveBookService.getReservationByUser(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testGetReservationByDni() {
        when(reserveBookRepository.findByUserDni("12345678")).thenReturn(Arrays.asList(reserveBook));
        List<ReserveBookResponse> result = reserveBookService.getReservationByDni("12345678");
        assertEquals(1, result.size());
    }

    @Test
    void testSaveReservation_Success() {
        ReserveBookRequest request = mock(ReserveBookRequest.class);
        when(request.getUserId()).thenReturn(1L);
        when(request.getLibraryId()).thenReturn(2L);
        when(request.getBookId()).thenReturn(1L);
        when(bookService.verifyBook(1L)).thenReturn(true);
        when(userService.getUser(1L)).thenReturn(user);
        when(userService.getUser(2L)).thenReturn(librarian);
        when(bookService.updateBookStock(1L, -1)).thenReturn(book);
        when(reserveBookRepository.save(any(ReserveBook.class))).thenReturn(reserveBook);
        ReserveBookResponse response = reserveBookService.saveReservation(request);
        assertEquals("Libro Test", response.getBookTitle());
        verify(bookService, times(2)).updateBookStock(1L, -1);
    }

    @Test
    void testSaveReservation_BookNotAvailable() {
        ReserveBookRequest request = mock(ReserveBookRequest.class);
        when(request.getBookId()).thenReturn(1L);
        when(bookService.verifyBook(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> reserveBookService.saveReservation(request));
    }

    @Test
    void testUpdateReservation() {
        ReserveBookUpdate update = mock(ReserveBookUpdate.class);
        when(reserveBookRepository.findById(1L)).thenReturn(Optional.of(reserveBook));
        when(update.getBookId()).thenReturn(1L);
        when(update.getId()).thenReturn(1L);
        when(update.getStartTime()).thenReturn(LocalTime.of(11,0));
        when(update.getEndTime()).thenReturn(LocalTime.of(13,0));
        when(update.getReservationDate()).thenReturn(LocalDate.now());
        when(update.getIsActive()).thenReturn(false);
        when(bookService.getBook(1L)).thenReturn(book);
        when(reserveBookRepository.save(any(ReserveBook.class))).thenReturn(reserveBook);
        ReserveBookResponse response = reserveBookService.updateReservation(update, 1L);
        assertEquals("Libro Test", response.getBookTitle());
        assertFalse(response.isActive());
    }

    @Test
    void testUpdateReservation_NotFound() {
        ReserveBookUpdate update = mock(ReserveBookUpdate.class);
        when(reserveBookRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reserveBookService.updateReservation(update, 2L));
    }

    @Test
    void testDeleteReservation() {
        when(reserveBookRepository.findById(1L)).thenReturn(Optional.of(reserveBook));
        when(reserveBookRepository.save(any(ReserveBook.class))).thenReturn(reserveBook);
        reserveBookService.deleteReservation(1L);
        verify(reserveBookRepository).save(reserveBook);
        assertFalse(reserveBook.getState());
    }

    @Test
    void testDeleteReservation_NotFound() {
        when(reserveBookRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reserveBookService.deleteReservation(2L));
    }
}

