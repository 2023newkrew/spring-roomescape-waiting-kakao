package nextstep.reservationwaiting;

import nextstep.reservation.ReservationRequest;

public class ReservationWaitingRequest {
    private Long scheduleId;

    private ReservationWaitingRequest() {}

    public ReservationWaitingRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public ReservationRequest toReservationRequest() {
        return new ReservationRequest(scheduleId);
    }
}
