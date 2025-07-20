package com.WebBiblioteca.Controller;

import com.WebBiblioteca.DTO.CustomerUserDetails;
import com.WebBiblioteca.DTO.Loan.LoanRequest;
import com.WebBiblioteca.DTO.Loan.LoanResponse;
import com.WebBiblioteca.DTO.Loan.LoanUpdateRequest;
import com.WebBiblioteca.Model.LoanState;
import com.WebBiblioteca.Model.Role;
import com.WebBiblioteca.Service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/userByState/{state}/{userCode}")
    public ResponseEntity<?> getLoanStateByUserCode(@PathVariable LoanState state, @PathVariable Long userCode) {
        return ResponseEntity.ok(loanService.getLoanStateByUserCode(state, userCode));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getLoansByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.getLoansByUserId(userId));
    }

    @GetMapping("/searchBook/{idUser}")
    public ResponseEntity<?> getLoansByBookTitleOrAuthorNameOrAuthorLastName(@RequestParam(name = "searchTerm") String searchTerm, @PathVariable Long idUser) {
        System.out.println("Search Term: " + searchTerm+" User ID: " + idUser);
        return ResponseEntity.ok(loanService.getLoansByBookTitleOrAuthorNameOrAuthorLastName(idUser,searchTerm));
    }

    @GetMapping("/user/dni/{dni}")
    public ResponseEntity<?> getLoansByUserId(@PathVariable String dni) {
        return ResponseEntity.ok(loanService.getLoansByUserDni(dni, Role.USER));
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getLoanById(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @PostMapping("/add")
    public ResponseEntity<?> addLoan(@RequestBody LoanRequest loanRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
        LoanResponse loanResponse = loanService.saveLoan(loanRequest,userDetails.getId());
        URI location = URI.create("/api/loan/" + loanResponse.getIdLean());
        return ResponseEntity.created(location).body(loanResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLoan(@PathVariable Long id, @RequestBody LoanUpdateRequest loanRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
            return ResponseEntity.ok(loanService.updateLoan(id, userDetails.getId(),loanRequest));
        }catch (Exception e){
            System.out.println("Error updating loan: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error updating loan: " + e.getMessage());
        }

    }
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @PutMapping("/updateState/{id}")
    public ResponseEntity<?> updateLoanState(@PathVariable Long id, @RequestParam String state) {
        return ResponseEntity.ok(loanService.updateState(id, state));
    }
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @PutMapping("/finished/{id}")
    public ResponseEntity<?> finishedLoan(@PathVariable Long id){
        return ResponseEntity.ok(loanService.finishedLoan(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updatePartialLoan(@PathVariable Long id, @RequestBody LoanUpdateRequest loanUpdateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(loanService.updateLoan(id, userDetails.getId() ,loanUpdateRequest));
    }
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }


}
