package espe.edu.ec.herreraalan_examen.Model;

public class RoomReservation {
    private Long id;
    private String roomCode;
    private String reservedByEmail;
    private int hours;
    private Status status;

    public enum Status {CREATED, CONFIRMED}

    public RoomReservation(Long id, String roomCode, String reservedByEmail, int hours, Status status) {
        this.id = id;
        this.roomCode = roomCode;
        this.reservedByEmail = reservedByEmail;
        this.hours = hours;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getReservedByEmail() {
        return reservedByEmail;
    }

    public void setReservedByEmail(String reservedByEmail) {
        this.reservedByEmail = reservedByEmail;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
