package nextstep.waitingreservation;

public class WaitingReservationRequest {
    private Long scheduleId;

    public WaitingReservationRequest() {
    }

    public WaitingReservationRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}
