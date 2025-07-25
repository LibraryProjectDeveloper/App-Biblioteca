package com.WebBiblioteca.Controller;

import com.WebBiblioteca.DTO.ReserveBook.ReserveBookReportRequest;
import com.WebBiblioteca.DTO.ReserveBook.ReserveBookRequest;
import com.WebBiblioteca.DTO.ReserveBook.ReserveBookResponse;
import com.WebBiblioteca.DTO.ReserveBook.ReserveBookUpdate;
import com.WebBiblioteca.Service.ReserveBookService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDate;


@RestController
@RequestMapping("/api/reserve")
public class ReserveBookController {
    private final ReserveBookService reserveBookService;
    public ReserveBookController(ReserveBookService reserveBookService) {
        this.reserveBookService = reserveBookService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getReservationAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(reserveBookService.getReservationList(page, size));
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

    @GetMapping("/book/{title}")
    public ResponseEntity<?> getReservationByBookTitle(@PathVariable String title) {
        return ResponseEntity.ok(reserveBookService.getReservationsByBookTitle(title));
    }

    @GetMapping("/searchReserveBookAut/{query}/{idUser}")
    public ResponseEntity<?> getReservationByBookTitleOrAuthorNameOrAuthorLastName(
            @PathVariable String query,
            @PathVariable Long idUser) {
        return ResponseEntity.ok(reserveBookService.getReservationsByBookTitleOrAuthorNameOrAuthorLastName(query, idUser));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<?> getReservationByDate(@PathVariable String date) {
        return ResponseEntity.ok(reserveBookService.getReservationByDate(LocalDate.parse(date)));
    }

    @GetMapping("/searchDateByUser/{date}/user/{userCode}")
    public ResponseEntity<?> getReservationByDateAndUserCode(
            @PathVariable String date,
            @PathVariable Long userCode) {
        return ResponseEntity.ok(reserveBookService.getReservationByDateAndUserCode(LocalDate.parse(date), userCode));
    }

    @GetMapping("/searchByStateAndUser/{state}/user/{userCode}")
    public ResponseEntity<?> getReservationsByStateAndUserCode(
            @PathVariable boolean state,
            @PathVariable Long userCode) {
        return ResponseEntity.ok(reserveBookService.getReservationsByStateAndUserCode(state, userCode));
    }

    @PostMapping("/add")
    public ResponseEntity<?> saveReservation(@RequestBody ReserveBookRequest reserveBookRequest) {
        ReserveBookResponse reserveBookCreated = reserveBookService.saveReservation(reserveBookRequest);
        URI location = URI.create("/api/reserve"+reserveBookCreated.getId());
        return ResponseEntity.created(location).body(reserveBookCreated);
    }
    @PostMapping("/download-report")
    public ResponseEntity<?> createReport(@Valid @RequestBody ReserveBookReportRequest request){
        byte [] file = reserveBookService.createReportExcel(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDisposition(ContentDisposition.attachment().filename("Report.xlsx").build());
        return new ResponseEntity<>(file,headers, HttpStatus.OK);
    }
    @GetMapping("/report")
    public ResponseEntity<?> getReserveDataReport(
            @RequestParam(value = "dateStart") String dateStart,
            @RequestParam(value = "dateEnd") String dateEnd){
        return ResponseEntity.ok(reserveBookService.getReserveBookHistory(dateStart,dateEnd));
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

