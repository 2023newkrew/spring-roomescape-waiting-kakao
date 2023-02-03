package app.nextstep.dto;

import app.nextstep.domain.Reservation;

public class ReservationResponse {
    private Long id;
    private ScheduleResponse schedule;
    private MemberResponse member;
    private String status;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();;
        this.schedule = new ScheduleResponse(reservation.getSchedule());
        this.member = new MemberResponse(reservation.getMember());
        this.status = reservation.getStatus();
    }

    public Long getId() {
        return id;
    }

    public ScheduleResponse getSchedule() {
        return schedule;
    }

    public MemberResponse getMember() {
        return member;
    }

    public String getStatus() {
        return status;
    }
}
