package nextstep.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.reservation.Reservation;
import nextstep.schedule.Schedule;

@RequiredArgsConstructor
@Getter
public class ReservationDeleteEvent {
    private final Schedule schedule;

    public ReservationDeleteEvent(Reservation reservation) {
        this(reservation.getSchedule());
    }
}
