package nextstep.reservation.domain;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class ReservationWaiting {
    private Long id;
    private Schedule schedule;
    private Member member;
    private Long waitingNum;

    public ReservationWaiting(Long id, Schedule schedule, Member member, Long waitingNum) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.waitingNum = waitingNum;
    }

    public ReservationWaiting(Schedule schedule, Member member, Long waitingNum) {
        this(null, schedule, member, waitingNum);
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

    public Long getWaitingNum() {
        return waitingNum;
    }
}
