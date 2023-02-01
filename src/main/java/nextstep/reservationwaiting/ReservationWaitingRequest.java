package nextstep.reservationwaiting;

public class ReservationWaitingRequest {
    private Long scheduleId;

    private ReservationWaitingRequest() {}

    public ReservationWaitingRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}
