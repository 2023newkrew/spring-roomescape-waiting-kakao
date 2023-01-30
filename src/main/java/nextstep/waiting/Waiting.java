package nextstep.waiting;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

public class Waiting {
    private Long id;
    private Schedule schedule;
    private Member member;
    private Long waitNum;

    public Waiting(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
    }

    public Waiting(Long id, Schedule schedule, Member member) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
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

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    public void setWaitNum(Long waitNum) {
        this.waitNum = waitNum;
    }
}
