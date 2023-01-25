package nextstep.reservation;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class ReservationWaiting {

    private Long id;
    private Member member;
    private Schedule schedule;
    private int waitNum;

    public ReservationWaiting(Member member, Schedule schedule, int waitNum) {
        this.member = member;
        this.schedule = schedule;
        this.waitNum = waitNum;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public int getWaitNum() {
        return waitNum;
    }
}
