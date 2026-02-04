package espe.edu.ec.herreraalan_examen;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import espe.edu.ec.herreraalan_examen.DTO.ReservationResponse;
import espe.edu.ec.herreraalan_examen.Model.RoomReservation;
import espe.edu.ec.herreraalan_examen.Repository.ReservationRepository;
import espe.edu.ec.herreraalan_examen.Service.ReservationService;
import espe.edu.ec.herreraalan_examen.Service.UserPolicyClient;

public class ReservationServiceTest {
    private ReservationRepository reservationRepository;
    private UserPolicyClient userPolicyClient;
    private ReservationService service;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        userPolicyClient = mock(UserPolicyClient.class);
        service = new ReservationService(reservationRepository, userPolicyClient);
    }

    @Test
    void testCreateReservationSuccess() {
        when(reservationRepository.findByRoomCode("A101")).thenReturn(Optional.empty());
        when(userPolicyClient.isUserBlocked("AlanHerrera@espe.edu.ec")).thenReturn(false);
        RoomReservation savedReservation = new RoomReservation(1L, "A101", "AlanHerrera@espe.edu.ec", 2, RoomReservation.Status.CREATED);
        when(reservationRepository.save(any(RoomReservation.class))).thenReturn(savedReservation);

        ReservationResponse response = service.createReservation("A101", "AlanHerrera@espe.edu.ec", 2);

        assertTrue(response.isSuccess());
        assertEquals("Reserva creada exitosamente", response.getMessage());
        assertEquals(1L, response.getReservationId());
        verify(reservationRepository).findByRoomCode("A101");
        verify(userPolicyClient).isUserBlocked("AlanHerrera@espe.edu.ec");
        verify(reservationRepository).save(any(RoomReservation.class));
    }

    @Test
    void testCreateReservationInvalidEmail() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.createReservation("A101", "pedroteamaxd", 2)
        );
        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(userPolicyClient);
    }

    @Test
    void testCreateReservationInvalidHours() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () ->
                service.createReservation("A101", "AlanHerrera@espe.edu.ec", 0)
        );
        Exception exception2 = assertThrows(IllegalArgumentException.class, () ->
                service.createReservation("A101", "AlanHerrera@espe.edu.ec", 9)
        );
        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(userPolicyClient);
    }

    @Test
    void testCreateReservationRoomAlreadyReserved() {
        RoomReservation existingReservation = new RoomReservation(2L, "A101", "AlanHerrera@espe.edu.ec", 2, RoomReservation.Status.CREATED);
        when(reservationRepository.findByRoomCode("A101")).thenReturn(Optional.of(existingReservation));
        Exception exception = assertThrows(IllegalStateException.class, () ->
                service.createReservation("A101", "AlanHerrera@espe.edu.ec", 2)
        );
        verify(reservationRepository).findByRoomCode("A101");
        verifyNoInteractions(userPolicyClient);
    }

    @Test
    void testCreateReservationUserBlockedByPolicy() {
        when(reservationRepository.findByRoomCode("A101")).thenReturn(Optional.empty());
        when(userPolicyClient.isUserBlocked("AlanHerrera@espe.edu.ec")).thenReturn(true);
        Exception exception = assertThrows(IllegalStateException.class, () ->
                service.createReservation("A101", "AlanHerrera@espe.edu.ec", 2)
        );
        verify(reservationRepository).findByRoomCode("A101");
        verify(userPolicyClient).isUserBlocked("AlanHerrera@espe.edu.ec");
    }
}
