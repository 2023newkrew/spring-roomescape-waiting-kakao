package nextstep.domain.reservation;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationTransitionSnapshot {

    private List<ReservationProjection> reservationProjections;
    private LocalDateTime createdAt;

    public ReservationTransitionSnapshot(List<ReservationProjection> reservationProjections) {
        this.reservationProjections = reservationProjections;
        this.createdAt = LocalDateTime.now();
    }

    public List<ReservationProjection> getReservationProjections() {
        return reservationProjections;
    }

    public boolean isCreatedAfter(LocalDateTime localDateTime) {
        return createdAt.isAfter(localDateTime);
    }
}
