package nextstep.reservation;

@FunctionalInterface
public interface ReservationStatusTransit {
    ReservationStatus transitStatus(ReservationStatus status, String role);
}
