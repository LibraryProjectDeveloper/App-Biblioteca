package com.WebBiblioteca.DTO.ReserveBook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter @NoArgsConstructor @AllArgsConstructor
public class ReserveBookRequest {
    private int userId;
    private int libraryId;
    private List<Integer> bookIds;
}

