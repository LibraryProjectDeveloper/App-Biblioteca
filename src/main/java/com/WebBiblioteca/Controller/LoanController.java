package com.WebBiblioteca.Controller;

import com.WebBiblioteca.DTO.Loan.LoanRequest;
import com.WebBiblioteca.DTO.Loan.LoanResponse;
import com.WebBiblioteca.DTO.Loan.LoanUpdateRequest;
import com.WebBiblioteca.Model.LoanState;
import com.WebBiblioteca.Service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/loan")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }
    @GetMapping("")
    public ResponseEntity<?> getAllLoans(){
        return ResponseEntity.ok(loanService.getAllLoans());
    }
    @GetMapping("/state/{state}")
    public ResponseEntity<?> getLoansByState(@PathVariable LoanState state){
        return ResponseEntity.ok(loanService.getLoansByState(state));
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getLoansByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.getLoansByUserId(userId));
    }
    @GetMapping("/user/dni/{dni}")
    public ResponseEntity<?> getLoansByUserId(@PathVariable String dni) {
        return ResponseEntity.ok(loanService.getLoansByUserDni(dni));
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getLoanById(id));
    }
    @PostMapping("/add")
    public ResponseEntity<?> addLoan(@RequestBody LoanRequest loanRequest) {
        LoanResponse loanResponse = loanService.saveLoan(loanRequest);
        URI location = URI.create("/api/loan/" + loanResponse.getIdLean());
        return ResponseEntity.created(location).body(loanResponse);
    }
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLoan(@PathVariable Long id, @RequestBody LoanUpdateRequest loanRequest) {
        return ResponseEntity.ok(loanService.updateLoan(id, loanRequest));
    }
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @PutMapping("/updateState/{id}")
    public ResponseEntity<?> updateLoanState(@PathVariable Long id, @RequestParam String state) {
        return ResponseEntity.ok(loanService.updateState(id, state));
    }
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updatePartialLoan(@PathVariable Long id, @RequestBody LoanUpdateRequest loanUpdateRequest) {
        return ResponseEntity.ok(loanService.updateLoan(id, loanUpdateRequest));
    }
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }


}
