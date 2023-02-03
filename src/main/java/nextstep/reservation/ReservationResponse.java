package nextstep.reservation;

import nextstep.schedule.Schedule;

public class ReservationResponse {
    private final Long id;
    private final Schedule schedule;

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

    public static ReservationResponse fromEntity(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getSchedule()
        );
    }
}
