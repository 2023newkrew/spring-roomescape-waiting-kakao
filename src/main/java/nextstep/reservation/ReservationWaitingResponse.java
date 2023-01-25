package nextstep.reservation;

import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleResponse;

public class ReservationWaitingResponse {

    private Long id;
    private ScheduleResponse scheduleResponse;
    private int waitNum;

    public ReservationWaitingResponse(ReservationWaiting reservationWaiting) {
        this.id = reservationWaiting.getId();
        this.scheduleResponse = new ScheduleResponse(reservationWaiting.getSchedule());
        this.waitNum = reservationWaiting.getWaitNum();
    }

    public ReservationWaitingResponse(Long id, ScheduleResponse scheduleResponse, int waitNum) {
        this.id = id;
        this.scheduleResponse = scheduleResponse;
        this.waitNum = waitNum;
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
