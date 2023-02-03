package nextstep.reservation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.schedule.ScheduleResponse;

@RequiredArgsConstructor
@Getter
public class ReservationResponse {
    private final Long id;
    private final ScheduleResponse scheduleResponse;

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), ScheduleResponse.from(reservation.getSchedule()));
    }
}
