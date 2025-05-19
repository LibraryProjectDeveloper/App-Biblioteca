package com.WebBiblioteca.Repository;
import com.WebBiblioteca.Model.ReserveBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public interface ReserveBookRepository extends JpaRepository<ReserveBook, Long> {
    List<ReserveBook> findByState(boolean state);
    /*
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final HashMap<Long, ReserveBook> reservationMap = new HashMap<>();
    private final Map<Long, Set<Long>> reservationsByUserId = new HashMap<>();
    private final Map<Long, Set<Long>> reservationsByBookId = new HashMap<>();

    //listar reservas
    public Map<Long,ReserveBook> getReservationList(){
        return reservationMap;
    }

    public ReserveBook getReservationById(Long id){
        return reservationMap.get(id) != null ? reservationMap.get(id) : null;
    }

    public ReserveBook addReservation(ReserveBook reservation){
        Long id = idGenerator.getAndIncrement();
        reservation.setCodeReserve(id);
        reservationMap.put(id, reservation);
        addToIndex(reservationsByUserId, reservation.getUser().getCode(), id);
        addToIndex(reservationsByBookId, reservation.getBook().getCodeBook(), id);
        return reservation;
    }

    private void addToIndex(Map<Long, Set<Long>> index, Long key, Long reservationId) {
        index.computeIfAbsent(key, k -> new HashSet<>()).add(reservationId);
    }

    //obtener reservas por usuario
    public List<ReserveBook> getReservationByUserId(Long idUser){
        return reservationsByUserId.getOrDefault(idUser, Collections.emptySet()).stream()
                .map(reservationMap::get)
                .toList();
    }

    //obtener reservas por libro
    public List<ReserveBook> getReservationByBookId(Long idBook){
        return reservationsByBookId.getOrDefault(idBook, Collections.emptySet()).stream()
                .map(reservationMap::get)
                .toList();
    }
    //reservas activas
    public Map<Long,ReserveBook> reservetacionActives(){
        return reservationMap.entrySet().stream()
                .filter(entry -> entry.getValue().isActive())
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
    }

    public ReserveBook updateReservation(Long id, ReserveBook reservation){
        if(reservationMap.containsKey(id)){
            reservationMap.put(id, reservation);
            return reservation;
        } else {
            return null;
        }
    }

    public boolean deleteReservation(Long id) {
        ReserveBook reservation = reservationMap.remove(id);
        if (reservation != null) {
            reservationsByUserId.get(reservation.getUser().getCode()).remove(id);
            reservationsByBookId.get(reservation.getBook().getCodeBook()).remove(id);
            return true;
        }else{
            return false;
        }
    }

     */
}

