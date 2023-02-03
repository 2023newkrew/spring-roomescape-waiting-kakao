package nextstep.reservationwaitings;

public class ReservationResult {
    private enum ReservationStatus {
        RESERVATION, RESERVATION_WAITING;
    }

    private final Long id;
    private final ReservationStatus status;

    private ReservationResult(Long id, ReservationStatus status) {
        this.id = id;
        this.status = status;
    }

    public static ReservationResult createReservation(long id) {
        return new ReservationResult(id, ReservationStatus.RESERVATION);
    }

    public static ReservationResult createReservationWaiting(long id) {
        return new ReservationResult(id, ReservationStatus.RESERVATION_WAITING);
    }

    public Long getId() {
        return id;
    }

    public boolean isReservationWaiting() {
        return status == ReservationStatus.RESERVATION_WAITING;
    }
}
