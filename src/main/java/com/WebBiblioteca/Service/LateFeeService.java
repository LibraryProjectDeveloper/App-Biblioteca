package com.WebBiblioteca.Service;

import com.WebBiblioteca.Repository.LateFeeRepository;
import org.springframework.stereotype.Service;

@Service
public class LateFeeService {
    private final LateFeeRepository lateFeeRepository;

    public LateFeeService(LateFeeRepository lateFeeRepository){
        this.lateFeeRepository = lateFeeRepository;
    }
}
