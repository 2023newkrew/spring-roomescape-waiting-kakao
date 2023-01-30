package nextstep.reservationwaitings;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ReservationWaitingRequest {

    private final long scheduleId;

    @JsonCreator
    public ReservationWaitingRequest(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public long getScheduleId() {
        return scheduleId;
    }
}
