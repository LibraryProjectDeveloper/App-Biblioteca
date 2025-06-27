package com.WebBiblioteca.Controller;

import com.WebBiblioteca.Service.LateFeeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/latefee")
public class LateFeeController {
    private final LateFeeService lateFeeService;

    public LateFeeController(LateFeeService lateFeeService) {
        this.lateFeeService = lateFeeService;
    }
}
