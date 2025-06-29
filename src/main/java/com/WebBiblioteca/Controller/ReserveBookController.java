package com.WebBiblioteca.Controller;

import com.WebBiblioteca.DTO.ReserveBook.ReserveBookRequest;
import com.WebBiblioteca.DTO.ReserveBook.ReserveBookResponse;
import com.WebBiblioteca.DTO.ReserveBook.ReserveBookUpdate;
import com.WebBiblioteca.Service.ReserveBookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
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
    @GetMapping("/user/dni/{dni}")
    public ResponseEntity<?> getReservationByUser(@PathVariable String dni){
        return ResponseEntity.ok(reserveBookService.getReservationByDni(dni));
    }
    @PostMapping("/add")
    public ResponseEntity<?> saveReservation(@RequestBody ReserveBookRequest reserveBookRequest) {
        ReserveBookResponse reserveBookCreated = reserveBookService.saveReservation(reserveBookRequest);
        URI location = URI.create("/api/reserve"+reserveBookCreated.getId());
        return ResponseEntity.created(location).body(reserveBookCreated);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable Long id, @RequestBody ReserveBookUpdate reserveBookUpdate){
        return ResponseEntity.ok(reserveBookService.updateReservation(reserveBookUpdate,id));
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updatePartialReservation(@PathVariable Long id, @RequestBody ReserveBookUpdate reserveBookUpdate){
        return ResponseEntity.ok(reserveBookService.updateReservation(reserveBookUpdate,id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id){
        reserveBookService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}

