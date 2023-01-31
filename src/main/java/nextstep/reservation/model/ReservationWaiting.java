package nextstep.reservation.model;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ReservationWaiting {
    private final Long id;
    private final Long memberId;
    private final Long scheduleId;
    private final LocalDateTime appliedTime;

    public ReservationWaiting(Long id, Long memberId, Long scheduleId, LocalDateTime appliedTime) {
        this.id = id;
        this.memberId = memberId;
        this.scheduleId = scheduleId;
        this.appliedTime = appliedTime;
    }

    public ReservationWaiting(Long memberId, Long scheduleId, LocalDateTime appliedTime) {
        this(null, memberId, scheduleId, appliedTime);
    }

    public boolean isAppliedBy(Long memberId) {
        return this.getMemberId().equals(memberId);
    }
}
