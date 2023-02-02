package nextstep.reservationwaiting.dto;

import nextstep.reservationwaiting.ReservationWaiting;
import nextstep.reservationwaiting.ReservationWaitingStatus;
import nextstep.schedule.Schedule;

public class ReservationWaitingResponse {

    private final Long id;
    private final Schedule schedule;
    private final Long waitNum;
    private final ReservationWaitingStatus status;

    public static ReservationWaitingResponse of(ReservationWaiting reservationWaiting) {
        return new ReservationWaitingResponse(reservationWaiting.getId(), reservationWaiting.getSchedule(),
                reservationWaiting.getWaitNum(), reservationWaiting.getStatus());
    }

    protected ReservationWaitingResponse(Long id, Schedule schedule, Long waitNum, ReservationWaitingStatus status) {
        this.id = id;
        this.schedule = schedule;
        this.waitNum = waitNum;
        this.status = status;
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

    public ReservationWaitingStatus getStatus() {
        return status;
    }
}
