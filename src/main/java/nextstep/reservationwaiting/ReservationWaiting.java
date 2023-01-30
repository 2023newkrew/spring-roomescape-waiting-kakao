package nextstep.reservationwaiting;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

public class ReservationWaiting {
    private Long id;
    private Schedule schedule;
    private Member member;
    private ReservationWaitingStatus status;

    public ReservationWaiting() {
    }

    public ReservationWaiting(Schedule schedule, Member member, ReservationWaitingStatus status) {
        this.schedule = schedule;
        this.member = member;
        this.status = status;
    }

    public ReservationWaiting(Long id, Schedule schedule, Member member, ReservationWaitingStatus status) {
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

    public ReservationWaitingStatus getStatus() {
        return status;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }
}
