package espe.edu.ec.herreraalan_examen.Service;

import espe.edu.ec.herreraalan_examen.DTO.ReservationResponse;
import espe.edu.ec.herreraalan_examen.Model.RoomReservation;
import espe.edu.ec.herreraalan_examen.Repository.ReservationRepository;

import java.util.Optional;

public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserPolicyClient userPolicyClient;
    public ReservationService(ReservationRepository reservationRepository, UserPolicyClient userPolicyClient) {
        this.reservationRepository = reservationRepository;
        this.userPolicyClient = userPolicyClient;
    }
    public ReservationResponse createReservation(String roomCode, String email, int hours) {
        // Validaciones de negocio
        if (roomCode == null || roomCode.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de la sala no puede ser nulo o vacío");}
        if (email == null || email.isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Correo electrónico inválido");}
        if (hours <= 0 || hours > 8) {
            throw new IllegalArgumentException("El número de horas debe ser mayor a 0 y menor o igual a 8");}
        // Validar si la sala ya está reservada
        Optional<RoomReservation> existing = reservationRepository.findByRoomCode(roomCode);
        if (existing.isPresent()) {
            throw new IllegalStateException("La sala ya se encuentra reservada");}
        // Validar si el usuario está bloqueado
        if (userPolicyClient.isUserBlocked(email)) {
            throw new IllegalStateException("El usuario está bloqueado por políticas institucionales");}
        RoomReservation reservation = new RoomReservation(null, roomCode, email, hours, RoomReservation.Status.CREATED);
        RoomReservation saved = reservationRepository.save(reservation);
        return new ReservationResponse(true, "Reserva creada exitosamente", saved.getId());
    }
}
