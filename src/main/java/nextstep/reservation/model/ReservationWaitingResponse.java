package nextstep.reservation.model;

import nextstep.schedule.model.Schedule;

public class ReservationWaitingResponse {
    private final Long id;
    private final Schedule schedule;
    private final Long waitNum;

    public ReservationWaitingResponse(Long id, Schedule schedule, Long waitNum){
        this.id = id;
        this.schedule = schedule;
        this.waitNum = waitNum;
    }
}