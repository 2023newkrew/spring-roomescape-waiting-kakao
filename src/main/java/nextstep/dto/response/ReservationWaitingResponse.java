package nextstep.dto.response;

import nextstep.domain.reservationwaiting.ReservationWaiting;

public class ReservationWaitingResponse {

    private Long id;
    private ScheduleResponse scheduleResponse;
    private int waitNum;

    public ReservationWaitingResponse(ReservationWaiting reservationWaiting) {
        this.id = reservationWaiting.getId();
        this.scheduleResponse = new ScheduleResponse(reservationWaiting.getSchedule());
        this.waitNum = reservationWaiting.getWaitNum();
    }

    public Long getId() {
        return id;
    }

    public ScheduleResponse getSchedule() {
        return scheduleResponse;
    }

    public int getWaitNum() {
        return waitNum;
    }
}
