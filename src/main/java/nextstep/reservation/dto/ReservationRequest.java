package nextstep.reservation.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ReservationRequest {
    @Min(1L)
    @NotNull
    private Long scheduleId;

    protected ReservationRequest() {
    }

    public ReservationRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}
