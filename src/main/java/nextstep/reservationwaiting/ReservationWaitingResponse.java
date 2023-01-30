package nextstep.reservationwaiting;

import nextstep.schedule.Schedule;

public class ReservationWaitingResponse {
    private final Long id;
    private final Schedule schedule;
    private final Long waitNum;

    public ReservationWaitingResponse(Long id, Schedule schedule, Long waitNum) {
        this.id = id;
        this.schedule = schedule;
        this.waitNum = waitNum;
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

    public static ReservationWaitingResponse from(ReservationWaiting reservationWaiting) {
        return new ReservationWaitingResponse(reservationWaiting.getId(), reservationWaiting.getSchedule(), reservationWaiting.getWaitingNum());
    }
}
