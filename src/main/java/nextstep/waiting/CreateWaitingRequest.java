package nextstep.waiting;

public class CreateWaitingRequest {
    private Long scheduleId;

    public CreateWaitingRequest() {
    }

    public CreateWaitingRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}
