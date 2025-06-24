package com.WebBiblioteca.Controller;

import com.WebBiblioteca.Service.ReserveBookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/reserve")
public class ReserveBookController {
    private final ReserveBookService reserveBookService;
    public ReserveBookController(ReserveBookService reserveBookService) {
        this.reserveBookService = reserveBookService;
    }
    @GetMapping("/")
    public ResponseEntity<?> getReservationAll() {
       return ResponseEntity.ok(reserveBookService.getReservationList());
    }

    @GetMapping("/actives")
    public ResponseEntity<?> getReservationActives() {
        return ResponseEntity.ok(reserveBookService.getReservationActives());
    }
    @GetMapping("/inactives")
    public ResponseEntity<?> getReservationInactives() {
        return ResponseEntity.ok(reserveBookService.gerReservationInactive());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reserveBookService.getReservationById(id));
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getReservationByUser(@PathVariable Long id){
        return ResponseEntity.ok(reserveBookService.getReservationByUser(id));
    }
}

