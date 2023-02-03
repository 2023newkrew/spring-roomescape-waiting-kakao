package nextstep.domain.reservation;

import java.time.LocalDateTime;

public class ReservationTransitionSnapshot {

    private LocalDateTime startTime;
    private LocalDateTime createdAt;

    public ReservationTransitionSnapshot(LocalDateTime startTime) {
        this.startTime = startTime;
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public boolean isCreatedAfter(LocalDateTime localDateTime) {
        return createdAt.isAfter(localDateTime);
    }
}
