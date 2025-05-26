package com.WebBiblioteca.Service;

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
    public List<ReserveBook> getReservationList() {
        return reserveBookRepository.findAll();
    }

    public List<ReserveBook> getReservationActives() {
        return reserveBookRepository.findByState(true);
    }

}
