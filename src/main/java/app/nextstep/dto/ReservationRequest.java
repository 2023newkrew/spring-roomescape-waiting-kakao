package app.nextstep.dto;

import app.nextstep.domain.Member;
import app.nextstep.domain.Reservation;
import app.nextstep.domain.Schedule;

public class ReservationRequest {
    private Long scheduleId;

    public ReservationRequest() {
    }

    public ReservationRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public Reservation toReservation(Long memberId) {
        return new Reservation(
                new Schedule(scheduleId),
                new Member(memberId));
    }
}
