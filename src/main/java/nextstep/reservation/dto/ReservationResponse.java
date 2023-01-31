package nextstep.reservation.dto;

import nextstep.reservation.Reservation;
import nextstep.schedule.Schedule;

public class ReservationResponse {

    private Long id;
    private Schedule schedule;

    public static ReservationResponse of(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getSchedule());
    }

    public ReservationResponse(Long id, Schedule schedule) {
        this.id = id;
        this.schedule = schedule;
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }


}
