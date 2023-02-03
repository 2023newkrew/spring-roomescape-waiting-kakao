package nextstep.reservationwaiting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.schedule.Schedule;

@RequiredArgsConstructor
@Getter
public class ReservationWaitingResponse {
    private final Long id;
    private final Schedule schedule;
    private final Long waitNum;

    public static ReservationWaitingResponse from(ReservationWaiting reservationWaiting) {
        return new ReservationWaitingResponse(reservationWaiting.getId(), reservationWaiting.getSchedule(), reservationWaiting.getWaitNum());
    }
}
