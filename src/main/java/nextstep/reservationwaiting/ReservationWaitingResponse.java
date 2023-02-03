package nextstep.reservationwaiting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.schedule.ScheduleResponse;

@RequiredArgsConstructor
@Getter
public class ReservationWaitingResponse {
    private final Long id;
    private final ScheduleResponse scheduleResponse;
    private final Long waitNum;

    public static ReservationWaitingResponse from(ReservationWaiting reservationWaiting, Long waitNum) {
        return new ReservationWaitingResponse(reservationWaiting.getId(), ScheduleResponse.from(reservationWaiting.getSchedule()), waitNum);
    }
}
