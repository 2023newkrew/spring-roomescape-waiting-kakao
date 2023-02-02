package nextstep.reservation;

import auth.UserDetails;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private ReservationStatus status;

    public Reservation() {
        this.status = ReservationStatus.UNAPPROVED;
    }

    public Reservation(Schedule schedule, Member member) {
        super();
        this.schedule = schedule;
        this.member = member;
    }

    public Reservation(Long id, Schedule schedule, Member member, ReservationStatus status) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.status = status;
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

    public ReservationStatus getStatus() {
        return status;
    }

    public boolean sameMember(UserDetails member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }
}
