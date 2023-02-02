package nextstep.dto.response;

import nextstep.domain.reservation.Reservation;
import nextstep.domain.reservation.ReservationState;

public class ReservationResponse {
    private Long id;
    private ScheduleResponse scheduleResponse;
    private ReservationState state;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.scheduleResponse = new ScheduleResponse(reservation.getSchedule());
        this.state = reservation.getState();
    }

    public Long getId() {
        return id;
    }

    public ScheduleResponse getSchedule() {
        return scheduleResponse;
    }

    public ReservationState getState() {
        return state;
    }
}
