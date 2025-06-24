package com.WebBiblioteca.Service;

import com.WebBiblioteca.DTO.ReserveBook.ReserveBookResponse;
import com.WebBiblioteca.Exception.ResourceNotFoundException;
import com.WebBiblioteca.Model.ReserveBook;
import com.WebBiblioteca.Repository.ReserveBookRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ReserveBookService {
    private final ReserveBookRepository reserveBookRepository;
    public ReserveBookService(ReserveBookRepository reserveBookRepository) {
        this.reserveBookRepository = reserveBookRepository;
    }
    public List<ReserveBookResponse> getReservationList() {
        return reserveBookRepository.findAll().stream().map(reserveBook -> new ReserveBookResponse(
                reserveBook.getCodeReserve(),
                reserveBook.getBook().getTitle(),
                reserveBook.getUser().getName(),
                reserveBook.getUser().getName(),
                reserveBook.isActive(),
                reserveBook.getDateReserve(),
                reserveBook.getStartTime(),
                reserveBook.getEndTime()))
                .toList();
    }

    public List<ReserveBookResponse> getReservationActives() {
        return reserveBookRepository.findByState(true).stream().map(reserveBook -> new ReserveBookResponse(
                reserveBook.getCodeReserve(),
                reserveBook.getBook().getTitle(),
                reserveBook.getUser().getName(),
                reserveBook.getUser().getName(),
                reserveBook.isActive(),
                reserveBook.getDateReserve(),
                reserveBook.getStartTime(),
                reserveBook.getEndTime()))
                .toList();
    }
    public List<ReserveBookResponse> gerReservationInactive() {
        return reserveBookRepository.findByState(false).stream().map(reserveBook -> new ReserveBookResponse(
                reserveBook.getCodeReserve(),
                reserveBook.getBook().getTitle(),
                reserveBook.getUser().getName(),
                reserveBook.getUser().getName(),
                reserveBook.isActive(),
                reserveBook.getDateReserve(),
                reserveBook.getStartTime(),
                reserveBook.getEndTime()))
                .toList();
    }
    public ReserveBookResponse getReservationById(Long id){
        ReserveBook reserveBook = reserveBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ReserveBook", "id", id));
        return new ReserveBookResponse(
                reserveBook.getCodeReserve(),
                reserveBook.getBook().getTitle(),
                reserveBook.getUser().getName(),
                reserveBook.getUser().getName(),
                reserveBook.isActive(),
                reserveBook.getDateReserve(),
                reserveBook.getStartTime(),
                reserveBook.getEndTime()
        );
    }
    public List<ReserveBookResponse> getReservationByUser(Long id) {
        return reserveBookRepository.findByUserId(id).stream().map(reserveBook -> new ReserveBookResponse(
                reserveBook.getCodeReserve(),
                reserveBook.getBook().getTitle(),
                reserveBook.getUser().getName(),
                reserveBook.getUser().getName(),
                reserveBook.isActive(),
                reserveBook.getDateReserve(),
                reserveBook.getStartTime(),
                reserveBook.getEndTime()))
                .toList();
    }



}
