package nextstep.waiting;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

public class ReservationWaiting {

    private Long id;
    private Schedule schedule;
    private Member member;
    private Long waitNum;

    public ReservationWaiting() {
    }

    public ReservationWaiting(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
        this.waitNum = schedule.getNextWaitingNumber();
    }

    public ReservationWaiting(Long id, Schedule schedule, Member member, Long waitNum) {
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

    public boolean sameMember(Long memberId) {
        return Objects.equals(member.getId(), memberId);
    }
}
