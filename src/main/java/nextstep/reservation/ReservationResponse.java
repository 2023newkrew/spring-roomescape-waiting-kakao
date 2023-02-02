package nextstep.reservation;

import auth.UserDetails;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

public class ReservationResponse {
    private Long id;
    private Schedule schedule;
    private Member member;

    public ReservationResponse() {
    }

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.schedule = reservation.getSchedule();
        this.member = reservation.getMember();
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
