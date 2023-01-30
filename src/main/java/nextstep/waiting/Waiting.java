package nextstep.waiting;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class Waiting {
    private Long id;
    private Schedule schedule;
    private Member member;
    private Long waitNum;

    public Waiting() {
    }

    public Waiting(Schedule schedule, Member member) {
        this(null, schedule, member, null);
    }

    public Waiting(Long id, Schedule schedule, Member member, Long waitNum) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.waitNum = waitNum;
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

    public Long getWaitNum() {
        return waitNum;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
