package com.WebBiblioteca.Controller;

import com.WebBiblioteca.Model.ReserveBook;
import com.WebBiblioteca.Service.ReserveBookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reserve")
public class ReserveBookController {
    private final ReserveBookService reserveBookService;
    public ReserveBookController(ReserveBookService reserveBookService) {
        this.reserveBookService = reserveBookService;
    }
    @GetMapping("/")
    public Map<Long, ReserveBook> getReservationAll() {
        return reserveBookService.getReservationList();
    }
    @GetMapping("/actives")
    public Map<Long, ReserveBook> getReservationActives() {
        return reserveBookService.getReservationActives();
    }
    @GetMapping("/user/{idUser}")
    public List<ReserveBook> getReservationByUserId(@PathVariable Long idUser) {
        return reserveBookService.getReservationByUserId(idUser);
    }
    @GetMapping("/book/{idBook}")
    public List<ReserveBook> getReservationByBookId(@PathVariable Long idBook) {
        return reserveBookService.getReservationByBookId(idBook);
    }
    @PostMapping("/add")
    public ResponseEntity<?> addReservation(@Valid @RequestBody ReserveBook reserveBook){
        ReserveBook reserveBook1 = reserveBookService.addReservation(reserveBook);
        if (reserveBook1 != null) {
            return ResponseEntity.ok(reserveBook1);
        } else {
            return ResponseEntity.badRequest().body("Error adding reservation");
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable Long id, @Valid @RequestBody ReserveBook reserveBook) {
        ReserveBook updatedReservation = reserveBookService.updateReservation(id, reserveBook);
        if (updatedReservation != null) {
            return ResponseEntity.ok(updatedReservation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updatePartialReservation(@PathVariable Long id, @RequestBody ReserveBook reserveBook) {
        ReserveBook updatedReservation = reserveBookService.updateReservation(id, reserveBook);
        if (updatedReservation != null) {
            return ResponseEntity.ok(updatedReservation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        boolean isDeleted = reserveBookService.deleteReservation(id);
        if (isDeleted) {
            return ResponseEntity.ok("Reservation deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

