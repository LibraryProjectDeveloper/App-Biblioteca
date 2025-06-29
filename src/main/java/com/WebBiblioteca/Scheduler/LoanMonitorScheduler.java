package com.WebBiblioteca.Scheduler;

import com.WebBiblioteca.Model.LateFee;
import com.WebBiblioteca.Model.LateFeeState;
import com.WebBiblioteca.Model.Loan;
import com.WebBiblioteca.Model.LoanState;
import com.WebBiblioteca.Repository.LateFeeRepository;
import com.WebBiblioteca.Repository.LoanRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.List;

@Component
public class LoanMonitorScheduler {
    private final LoanRepository loanRepository;
    private final LateFeeRepository lateFeeRepository;

    public LoanMonitorScheduler(LoanRepository loanRepository, LateFeeRepository lateFeeRepository) {
        this.loanRepository = loanRepository;
        this.lateFeeRepository = lateFeeRepository;
    }
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkOverdueLoans() {
        List<Loan> overdueLoans = loanRepository.findByDevolutionDateBeforeAndState(LocalDateTime.now(), LoanState.PRESTADO);
        for (Loan loan : overdueLoans) {
            if(!lateFeeRepository.existsByLoanId(loan.getIdLoan())) {
                LateFee lateFee = new LateFee();
                lateFee.setAmount(calculateLateFee(loan));
                lateFee.setReason("Loan overdue");
                lateFee.setLateFeeDate(LocalDateTime.now());
                lateFee.setState(LateFeeState.PENDIENTE);
                lateFee.setLoan(loan);
                lateFeeRepository.save(lateFee);
            }
        }
    }
    private double calculateLateFee(Loan loan) {
        LocalDateTime now = LocalDateTime.now();
        long daysLate = java.time.Duration.between(loan.getDevolutionDate(), now).toDays();
        double lateFeeAmount = daysLate * 5;
        return Math.max(lateFeeAmount, 0);
    }
}
