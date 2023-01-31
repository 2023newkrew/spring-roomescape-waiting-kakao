package nextstep.waiting.dto;

public class ReservationWaitingRequest {
    private final Long scheduleId;

    public ReservationWaitingRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}
