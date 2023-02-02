package roomwaiting.nextstep.reservation.dto;

public class ReservationRequest {
    private Long scheduleId;
    private String name;

    public ReservationRequest() {
    }

    public ReservationRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}
