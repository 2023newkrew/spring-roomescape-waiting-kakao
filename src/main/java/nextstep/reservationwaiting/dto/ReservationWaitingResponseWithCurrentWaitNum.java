package nextstep.reservationwaiting.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import nextstep.reservationwaiting.ReservationWaiting;
import nextstep.reservationwaiting.ReservationWaitingStatus;
import nextstep.schedule.Schedule;

public class ReservationWaitingResponseWithCurrentWaitNum extends ReservationWaitingResponse {

    private final int currentWaitNum;

    @JsonCreator
    private ReservationWaitingResponseWithCurrentWaitNum(Long id, Schedule schedule, Long waitNum,
                                                         ReservationWaitingStatus status, int currentWaitNum) {
        super(id, schedule, waitNum, status);
        this.currentWaitNum = currentWaitNum;
    }

    public ReservationWaitingResponseWithCurrentWaitNum(ReservationWaiting reservationWaiting, int currentWaitNum) {
        this(reservationWaiting.getId(), reservationWaiting.getSchedule(), reservationWaiting.getWaitNum(),
                reservationWaiting.getStatus(),
                currentWaitNum);
    }

    public int getCurrentWaitNum() {
        return currentWaitNum;
    }
}
