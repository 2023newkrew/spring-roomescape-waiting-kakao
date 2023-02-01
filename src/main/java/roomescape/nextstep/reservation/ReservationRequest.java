package roomescape.nextstep.reservation;

import javax.validation.constraints.PositiveOrZero;

public class ReservationRequest {
    @PositiveOrZero
    private Long scheduleId;

    public ReservationRequest() {
    }

    public ReservationRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}
