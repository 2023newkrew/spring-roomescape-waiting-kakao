package nextstep.reservationwaiting;

import nextstep.reservation.Reservation;
import nextstep.schedule.Schedule;

public class ReservationWaitingResponse {

    private final Long id;
    private final Schedule schedule;
    private final Long waitNum;

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
