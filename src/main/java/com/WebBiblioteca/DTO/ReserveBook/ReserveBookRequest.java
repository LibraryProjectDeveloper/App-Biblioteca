package com.WebBiblioteca.DTO.ReserveBook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor
public class ReserveBookRequest {
    private Long userId;
    private Long libraryId;
    private Long bookId;
}

