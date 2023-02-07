package nextstep.reservation.domain;

import java.util.Objects;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;

    public Reservation() {
    }

    public Reservation(Schedule schedule, Member member) {
        this(null, schedule, member);
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

    public void setId(Long id) {
        this.id = id;
    }

    public boolean sameMember(Member member) {
        return Objects.nonNull(member) && Objects.equals(this.member.getId(), member.getId());
    }
}
