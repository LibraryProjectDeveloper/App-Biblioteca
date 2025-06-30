package com.WebBiblioteca.Advince;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
@AllArgsConstructor
@Getter
public class ErrorResponse {
    private int status;
    private LocalDateTime date;
    private String message;
    private String details;
}
