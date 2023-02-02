package nextstep.domain.reservation;

import nextstep.domain.member.Member;
import nextstep.domain.schedule.Schedule;

import java.util.Objects;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private ReservationState state;

    public Reservation() {
    }

    public Reservation(Schedule schedule, Member member, ReservationState state) {
        this.schedule = schedule;
        this.member = member;
        this.state = state;
    }

    public Reservation(Long id, Schedule schedule, Member member, ReservationState state) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.state = state;
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

    public ReservationState getState() {
        return state;
    }

    public boolean sameMember(Long memberId) {
        return member != null && Objects.equals(this.member.getId(), memberId);
    }
}
