package com.WebBiblioteca.DTO.ReserveBook;


import com.WebBiblioteca.Model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ReserveBookResponse {
    private Long id;
    private String bookTitle;
    private Long idUser;
    private String userName;
    private Long librarianId;
    private String libraryName;
    private boolean isActive;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String authorBook;
    private Category categoryBook;
}
