package nextstep.reservationwaiting.dto;

import nextstep.reservationwaiting.ReservationWaiting;
import nextstep.schedule.Schedule;

public class ReservationWaitingResponse {

    private final Long id;
    private final Schedule schedule;
    private final Long waitNum;

    public static ReservationWaitingResponse of(ReservationWaiting reservationWaiting) {
        return new ReservationWaitingResponse(reservationWaiting.getId(), reservationWaiting.getSchedule(), reservationWaiting.getWaitNum());
    }

    private ReservationWaitingResponse(Long id, Schedule schedule, Long waitNum) {
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
}
