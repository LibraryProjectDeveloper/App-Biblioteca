package com.WebBiblioteca.Controller;

import com.WebBiblioteca.Service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/{state}")
    public ResponseEntity<?> getLoansByState(@PathVariable String state){
        return ResponseEntity.ok(loanService.getLoansByState(state));
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getLoansByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.getLoansByUserId(userId));
    }

}
