package nextstep.reservationwaiting;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ReservationWaitingRequest {
    private final Long scheduleId;

    @JsonCreator
    public ReservationWaitingRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}
