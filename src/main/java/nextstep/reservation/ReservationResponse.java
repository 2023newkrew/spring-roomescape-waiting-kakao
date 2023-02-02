package nextstep.reservation;

import nextstep.schedule.Schedule;

public class ReservationResponse {
    private Long id;
    private Schedule schedule;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.schedule = reservation.getSchedule();
    }

    /* RestAssured에서 사용 */
    @SuppressWarnings("unused")
    public ReservationResponse() {
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
