package espe.edu.ec.herreraalan_examen.Repository;
import espe.edu.ec.herreraalan_examen.Model.RoomReservation;
import java.util.Optional;

public interface ReservationRepository {
    Optional<RoomReservation> findByRoomCode(String roomCode);
    RoomReservation save(RoomReservation reservation);
    boolean existsByRoomNumber(String roomCode);
}
