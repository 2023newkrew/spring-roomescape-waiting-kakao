package nextstep.domain.reservation;

import nextstep.domain.member.Member;
import nextstep.domain.schedule.Schedule;

import java.util.Objects;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;

    public Reservation() {
    }

    public Reservation(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
    }

    public Reservation(Long id, Schedule schedule, Member member) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
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

    public boolean sameMember(Long memberId) {
        return member != null && Objects.equals(this.member.getId(), memberId);
    }
}
