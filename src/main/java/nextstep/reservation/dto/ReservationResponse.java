package nextstep.reservation.dto;

import nextstep.reservation.domain.Reservation;
import nextstep.schedule.domain.Schedule;

import java.util.Objects;

public class ReservationResponse {
    private Long id;
    private Schedule schedule;

    private ReservationResponse(Long id, Schedule schedule) {
        this.id = id;
        this.schedule = schedule;
    }

    public static ReservationResponse of(Reservation reservation){
        return new ReservationResponse(
                reservation.getId(),
                reservation.getSchedule()
        );
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
