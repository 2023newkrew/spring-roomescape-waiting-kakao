package nextstep.waiting;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class Waiting {
    private Long id;
    private Schedule schedule;
    private Member member;

    public Waiting() {
    }

    public Waiting(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
    }

    public Waiting(Long id, Schedule schedule, Member member) {
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
}
