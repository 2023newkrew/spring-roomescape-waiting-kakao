package nextstep.reservation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.schedule.Schedule;

@RequiredArgsConstructor
@Getter
public class ReservationDeleteEvent {
    private final Schedule schedule;

    public ReservationDeleteEvent(Reservation reservation) {
        this(reservation.getSchedule());
    }
}
