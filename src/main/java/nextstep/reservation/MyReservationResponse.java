package nextstep.reservation;

import nextstep.schedule.Schedule;

public class MyReservationResponse {
    private final Long id;
    private final Schedule schedule;

    private MyReservationResponse(Long id, Schedule schedule) {
        this.id = id;
        this.schedule = schedule;
    }

    public static MyReservationResponse from(Reservation reservation) {
        return new MyReservationResponse(reservation.getId(), reservation.getSchedule());
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
