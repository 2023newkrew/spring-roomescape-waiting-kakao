package nextstep.domain.reservation;

import java.time.LocalDateTime;

public class ReservationSearch {

    private ReservationStatus status;
    private Long lastReservationId;
    private LocalDateTime start;
    private int chunkSize;

    public ReservationSearch(ReservationStatus status, Long lastReservationId, LocalDateTime start, int chunkSize) {
        this.status = status;
        this.lastReservationId = lastReservationId;
        this.start = start;
        this.chunkSize = chunkSize;
    }

    public void setLastReservationId(Long lastReservationId) {
        this.lastReservationId = lastReservationId;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public ReservationStatus getNextStatus() {
        return status.getNextStatus();
    }

    public Long getLastReservationId() {
        return lastReservationId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    @Override
    public String toString() {
        return "ReservationSearch{" +
                "status=" + status +
                ", lastReservationId=" + lastReservationId +
                ", start=" + start +
                ", chunkSize=" + chunkSize +
                '}';
    }
}
