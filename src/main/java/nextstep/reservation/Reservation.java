package nextstep.reservation;

import lombok.Getter;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

@Getter
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

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }
}
