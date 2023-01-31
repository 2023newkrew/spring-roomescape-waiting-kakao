package nextstep.reservation;

public class ReservationRequest {
    private Long scheduleId;

    /* RequestBody에서 사용 */
    @SuppressWarnings("unused")
    public ReservationRequest() {
    }

    public ReservationRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}
