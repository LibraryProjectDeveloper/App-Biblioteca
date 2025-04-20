package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.RerserveBoock;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RervaRepository {
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final HashMap<Long, RerserveBoock> reservationMap = new HashMap<>();
    private final Map<Long, Set<Long>> reservationsByUserId = new HashMap<>();
    private final Map<Long, Set<Long>> reservationsByBookId = new HashMap<>();

    //listar reservas
    public Map<Long,RerserveBoock> getReservationList(){
        return reservationMap;
    }

    public RerserveBoock getReservationById(Long id){
        return reservationMap.get(id) != null ? reservationMap.get(id) : null;
    }

    public RerserveBoock addReservation(RerserveBoock reservation){
        Long id = idGenerator.getAndIncrement();
        reservation.setCodeReserve(id);

        if (reservation.getDateReserve() == null) {
            reservation.setDateReserve(LocalDate.now());
        }

        reservation.setState(true);
        reservationMap.put(id, reservation);

        addToIndex(reservationsByUserId, reservation.getUser().getCode(), id);
        addToIndex(reservationsByBookId, reservation.getReserveBook().getCodeBook(), id);
        return reservation;
    }

    private void addToIndex(Map<Long, Set<Long>> index, Long key, Long reservationId) {
        index.computeIfAbsent(key, k -> new HashSet<>()).add(reservationId);
    }

    //obtener reservas por usuario
    public List<RerserveBoock> getReservationByUserId(Long idUser){
        return reservationsByUserId.getOrDefault(idUser, Collections.emptySet()).stream()
                .map(reservationMap::get)
                .toList();
    }

    //obtener reservas por libro
    public List<RerserveBoock> getReservationByBookId(Long idBook){
        return reservationsByBookId.getOrDefault(idBook, Collections.emptySet()).stream()
                .map(reservationMap::get)
                .toList();
    }
    //reservas activas
    public Map<Long,RerserveBoock> reservetacionActives(){
        return reservationMap.entrySet().stream()
                .filter(entry -> entry.getValue().isActive())
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
    }

    public RerserveBoock updateReservation(Long id, RerserveBoock reservation){
        return reservationMap.computeIfPresent(id, (key, value) -> {
            value.setDateReserve(reservation.getDateReserve());
            value.setState(reservation.getState());
            value.setStartTime(reservation.getStartTime());
            value.setEndTime(reservation.getEndTime());
            return value;
        });
    }

    public void deleteReservation(Long id) {
        RerserveBoock reservation = reservationMap.remove(id);
        if (reservation != null) {
            reservationsByUserId.get(reservation.getUser().getCode()).remove(id);
            reservationsByBookId.get(reservation.getReserveBook().getCodeBook()).remove(id);
        }
    }
    public RerserveBoock updatePartial(Long id, RerserveBoock reservation){
        return reservationMap.computeIfPresent(id, (key, value) -> {
            if (reservation.getDateReserve() != null) {
                value.setDateReserve(reservation.getDateReserve());
            }
            if (reservation.getState() != null) {
                value.setState(reservation.getState());
            }
            if (reservation.getStartTime() != null) {
                value.setStartTime(reservation.getStartTime());
            }
            if (reservation.getEndTime() != null) {
                value.setEndTime(reservation.getEndTime());
            }
            return value;
        });

    }
}
