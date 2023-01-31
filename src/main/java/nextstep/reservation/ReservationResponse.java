package nextstep.reservation;

import nextstep.schedule.Schedule;

public class ReservationResponse {
    private Long id;
    private Schedule schedule;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.schedule = reservation.getSchedule();
    }

    public ReservationResponse() {
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
