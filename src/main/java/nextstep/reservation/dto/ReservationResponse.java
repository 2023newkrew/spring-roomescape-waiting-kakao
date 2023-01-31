package nextstep.reservation.dto;

import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.schedule.Schedule;

public class ReservationResponse {
    private final Long id;
    private final Schedule schedule;
    private final Member member;

    private ReservationResponse(Long id, Schedule schedule, Member member) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
    }

    public static ReservationResponse of(Reservation reservation){
        return new ReservationResponse(reservation.getId(), reservation.getSchedule(), reservation.getMember());
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Member getMember() {
        return member;
    }
}
