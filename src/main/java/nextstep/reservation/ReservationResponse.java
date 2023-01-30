package nextstep.reservation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.schedule.Schedule;

@RequiredArgsConstructor
@Getter
public class ReservationResponse {
    private final Long id;
    private final Schedule schedule;

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getSchedule());
    }
}
