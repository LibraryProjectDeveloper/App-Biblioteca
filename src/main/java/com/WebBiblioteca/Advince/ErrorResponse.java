package com.WebBiblioteca.Advince;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@AllArgsConstructor
@Getter @Setter
public class ErrorResponse {
    private int status;
    private LocalDateTime date;
    private String message;
    private String details;
}
