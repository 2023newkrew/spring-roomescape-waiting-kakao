package nextstep.reservationwaiting;

import nextstep.schedule.Schedule;

public class ReservationWaitingResponse {

    private Long id;
    private Schedule schedule;
    private Long waitNum;

    public ReservationWaitingResponse() {
    }

    public ReservationWaitingResponse(ReservationWaiting reservationWaiting) {
        this.id = reservationWaiting.getId();
        this.schedule = reservationWaiting.getSchedule();
        this.waitNum = reservationWaiting.getWaitNum();
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Long getWaitNum() {
        return waitNum;
    }
}
