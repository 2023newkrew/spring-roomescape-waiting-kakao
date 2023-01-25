package nextstep.reservation;

import nextstep.schedule.ScheduleResponse;

public class ReservationResponse {
    private Long id;
    private ScheduleResponse scheduleResponse;

    public ReservationResponse(Reservation reservation){
        this.id = reservation.getId();
        this.scheduleResponse = new ScheduleResponse(reservation.getSchedule());
    }

    public Long getId() {
        return id;
    }

    public ScheduleResponse getSchedule() {
        return scheduleResponse;
    }
}
