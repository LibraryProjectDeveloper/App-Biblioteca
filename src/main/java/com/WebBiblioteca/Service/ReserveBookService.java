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
    public ReserveBook updateReservation(Long id, ReserveBook reservation) {
        ReserveBook reserveBook = reserveBookRepository.getReservationById(id);
        if(reserveBook != null && reservation != null){
            if(reservation.getDateReserve() != null){
                reserveBook.setDateReserve(reservation.getDateReserve());
            }
            if(reservation.getStartTime() != null){
                reserveBook.setStartTime(reservation.getStartTime());
            }
            if(reservation.getEndTime() != null){
                reserveBook.setEndTime(reservation.getEndTime());
            }
            if(reservation.getUser() != null){
                reserveBook.setUser(reservation.getUser());
            }
            if(reservation.getBook() != null){
                reserveBook.setBook(reservation.getBook());
            }
            if(reservation.getState() != null){
                reserveBook.setState(reservation.getState());
            }
            return reserveBookRepository.updateReservation(id, reserveBook);

        }
        return null;
    }

    public boolean deleteReservation(Long id) {
        return reserveBookRepository.deleteReservation(id);
    }

     */
}
