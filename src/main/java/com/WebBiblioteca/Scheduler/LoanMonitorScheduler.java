package com.WebBiblioteca.Scheduler;

import com.WebBiblioteca.Model.LateFee;
import com.WebBiblioteca.Model.LateFeeState;
import com.WebBiblioteca.Model.Loan;
import com.WebBiblioteca.Model.LoanState;
import com.WebBiblioteca.Repository.LateFeeRepository;
import com.WebBiblioteca.Repository.LoanRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class LoanMonitorScheduler {
    private final LoanRepository loanRepository;
    private final LateFeeRepository lateFeeRepository;

    public LoanMonitorScheduler(LoanRepository loanRepository, LateFeeRepository lateFeeRepository) {
        this.loanRepository = loanRepository;
        this.lateFeeRepository = lateFeeRepository;
    }


    @Scheduled(cron = "0 0 0 * * * ")
    @Transactional
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
            }else{
                Optional<LateFee> lateFeeOptional = lateFeeRepository.getLateFeeByLoan(loan.getIdLoan());
                if(lateFeeOptional.isPresent()){
                    LateFee lateFee = lateFeeOptional.get();
                    lateFee.setAmount(calculateLateFee(loan));
                    lateFeeRepository.save(lateFee);
                }
            }
            loan.setState(LoanState.RETRASADO);
            loanRepository.save(loan);
        }
    }
    private double calculateLateFee(Loan loan) {
        LocalDateTime now = LocalDateTime.now();
        long daysLate = java.time.Duration.between(loan.getDevolutionDate(), now).toDays();
        double lateFeeAmount = daysLate * 5;
        return Math.max(lateFeeAmount, 0);
    }
}
