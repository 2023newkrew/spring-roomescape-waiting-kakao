package nextstep.reservation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ReservationRequest {
    private final Long scheduleId;

    @JsonCreator
    public ReservationRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}
