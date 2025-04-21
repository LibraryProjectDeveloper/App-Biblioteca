package com.WebBiblioteca.Service;

import com.WebBiblioteca.Model.Book;
import com.WebBiblioteca.Model.ReserveBook;
import com.WebBiblioteca.Model.Role;
import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Repository.BookReposity;
import com.WebBiblioteca.Repository.ReserveBookRepository;
import com.WebBiblioteca.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ReserveBookTest {

    private final ReserveBookRepository reserveBookRepository = new ReserveBookRepository();
    private final ReserveBookService reserveBookService = new ReserveBookService(reserveBookRepository);
    private final UserRepository userRepository = new UserRepository();
    private final UserService userService = new UserService(userRepository);
    private final BookReposity bookReposity = new BookReposity();
    private final BookService bookService = new BookService(bookReposity);

    @Test
    void testAddReservation() {
        ReserveBook reservation = new ReserveBook();
        userService.addUser(new User(1L,"Renato","Ballena","renato@gmail.com","987654321","Manco capac 249","87654321","clave123",true, LocalDateTime.now(), Role.USER));
        bookService.addBook(new Book(1L,"Los perros hambrientos", LocalDate.now(),"Ciro Alegria","Novela",10));
        reservation.setDateReserve(LocalDate.now().plusDays(1));
        reservation.setStartTime(LocalTime.of(10, 0));
        reservation.setEndTime(LocalTime.of(12, 0));
        User user = userService.getAllUsers().get(1L);
        Book book = bookService.getBookList().get(1L);
        reservation.setUser(user);
        reservation.setBook(book);
        ReserveBook savedReservation = reserveBookService.addReservation(reservation);
        assertNotNull(savedReservation);
        assertEquals(1L, savedReservation.getCodeReserve());
        assertEquals(user.getCode(), savedReservation.getUser().getCode());
        assertEquals(book.getCodeBook(), savedReservation.getBook().getCodeBook());
    }
    @Test
    void testGetAllReservations() {
        ReserveBook reservation1 = createSampleReservation();
        ReserveBook reservation2 = createSampleReservation();
        reservation2.setDateReserve(LocalDate.now().plusDays(2));
        reserveBookService.addReservation(reservation1);
        reserveBookService.addReservation(reservation2);
        Map<Long,ReserveBook> allReservations = reserveBookService.getReservationList();
        assertEquals(2, allReservations.size());
    }

    @Test
    void testGetReservationByUserId() {
        User user = createSampleReservation().getUser();
        ReserveBook reservation1 = createSampleReservation();
        ReserveBook reservation2 = createSampleReservation();
        reservation2.setDateReserve(LocalDate.now().plusDays(2));
        reserveBookService.addReservation(reservation1);
        reserveBookService.addReservation(reservation2);
        List<ReserveBook> userReservations = reserveBookService.getReservationByUserId(user.getCode());
        assertEquals(2, userReservations.size());
        userReservations.forEach(res -> assertEquals(user.getCode(), res.getUser().getCode()));
    }

    @Test
    void testGetReservationByBookId() {
        Book book = createSampleReservation().getBook();
        ReserveBook reservation1 = createSampleReservation();
        ReserveBook reservation2 = createSampleReservation();
        reservation2.setDateReserve(LocalDate.now().plusDays(2));
        reserveBookService.addReservation(reservation1);
        reserveBookService.addReservation(reservation2);
        List<ReserveBook> bookReservations = reserveBookService.getReservationByBookId(book.getCodeBook());
        assertEquals(2, bookReservations.size());
        bookReservations.forEach(res -> assertEquals(book.getCodeBook(), res.getBook().getCodeBook()));
    }

    @Test
    void testUpdateReservation_Success() {
        ReserveBook reservation = createSampleReservation();
        ReserveBook savedReservation = reserveBookService.addReservation(reservation);
        ReserveBook updatedInfo = new ReserveBook();
        updatedInfo.setStartTime(LocalTime.of(14, 0));
        updatedInfo.setEndTime(LocalTime.of(16, 0));
        updatedInfo.setState(false);
        ReserveBook result = reserveBookService.updateReservation(savedReservation.getCodeReserve(), updatedInfo);
        assertNotNull(result);
        assertEquals(LocalTime.of(14, 0), result.getStartTime());
        assertEquals(LocalTime.of(16, 0), result.getEndTime());
    }

    @Test
    void testUpdateReservation_NotFound() {
        ReserveBook updatedInfo = new ReserveBook();
        updatedInfo.setStartTime(LocalTime.of(14, 0));
        ReserveBook result = reserveBookService.updateReservation(999L, updatedInfo);
        assertNull(result);
    }

    @Test
    void testDeleteReservation_Success() {
        ReserveBook reservation = createSampleReservation();
        ReserveBook savedReservation = reserveBookService.addReservation(reservation);
        boolean result = reserveBookService.deleteReservation(savedReservation.getCodeReserve());
        assertTrue(result);
        assertNull(reserveBookRepository.getReservationById(savedReservation.getCodeReserve()));
    }

    @Test
    void testDeleteReservation_NotFound() {
        boolean result = reserveBookService.deleteReservation(999L);
        assertFalse(result);
    }

    private ReserveBook createSampleReservation() {
        ReserveBook reservation = new ReserveBook();
        reservation.setDateReserve(LocalDate.now().plusDays(1));
        reservation.setStartTime(LocalTime.of(10, 0));
        reservation.setEndTime(LocalTime.of(12, 0));
        reservation.setState(true);
        userService.addUser(new User(1L,"Renato","Ballena","renato@gmail.com","987654321","Manco capac 249","87654321","clave123",true, LocalDateTime.now(), Role.USER));
        bookService.addBook(new Book(1L,"Los perros hambrientos", LocalDate.now(),"Ciro Alegria","Novela",10));
        User user = userService.getAllUsers().get(1L);
        Book book = bookService.getBookList().get(1L);
        reservation.setUser(user);
        reservation.setBook(book);
        return reservation;
    }
}
