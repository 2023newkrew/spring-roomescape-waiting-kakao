package nextstep.reservationwaiting.dto;

import nextstep.reservationwaiting.domain.ReservationWaiting;
import nextstep.schedule.domain.Schedule;

public class ReservationWaitingResponse {
    private Long id;
    private Schedule schedule;
    private int waitNum;

    private ReservationWaitingResponse(Long id, Schedule schedule, int waitNum) {
        this.id = id;
        this.schedule = schedule;
        this.waitNum= waitNum;
    }

    public static ReservationWaitingResponse of(ReservationWaiting reservationWaiting, int waitNum){
        return new ReservationWaitingResponse(
                reservationWaiting.getId(),
                reservationWaiting.getSchedule(),
                waitNum
        );
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public int getWaitNum() {
        return waitNum;
    }
}
