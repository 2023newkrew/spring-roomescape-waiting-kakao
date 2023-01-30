package nextstep.reservationwaiting;

import com.fasterxml.jackson.annotation.JsonCreator;
import nextstep.schedule.Schedule;

public class ReservationWaitingResponse {

    private final Long id;
    private final Schedule schedule;
    private final Long waitNum;

    @JsonCreator
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
