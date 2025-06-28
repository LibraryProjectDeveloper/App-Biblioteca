package com.WebBiblioteca.Service;

import com.WebBiblioteca.Model.ReserveBook;
import com.WebBiblioteca.Repository.ReserveBookRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ReserveBookService {
    private final ReserveBookRepository reserveBookRepository;
    public ReserveBookService(ReserveBookRepository reserveBookRepository) {
        this.reserveBookRepository = reserveBookRepository;
    }

    public List<ReserveBook> getReservationList() {
        return reserveBookRepository.findAll();
    }

    public List<ReserveBook> getReservationActives() {
        return reserveBookRepository.findByState(true);
    }
    /*
    public Map<Long, ReserveBook> getReservationList() {
        return reserveBookRepository.getReservationList();
    }
    public Map<Long, ReserveBook> getReservationActives() {
        return reserveBookRepository.reservetacionActives();
    }
    public List<ReserveBook> getReservationByUserId(Long idUser) {
        return reserveBookRepository.getReservationByUserId(idUser);
    }
    public List<ReserveBook> getReservationByBookId(Long idBook) {
        return reserveBookRepository.getReservationByBookId(idBook);
    }
    public ReserveBook addReservation(ReserveBook reservation) {
        if (reservation !=null) {
            if (reservation.getDateReserve() == null) {
                reservation.setDateReserve(LocalDate.now());
            }
            reservation.setState(true);
            return reserveBookRepository.addReservation(reservation);
        }
        return null;
    }
}
