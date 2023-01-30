package nextstep.reservationwaitings;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class ReservationWaitings {

    private final Long id;
    private final Member member;
    private final Schedule schedule;
    private final Long waitNum;

    public ReservationWaitings(Member member, Schedule schedule, long waitNum) {
        this(null, member, schedule, waitNum);
    }

    public ReservationWaitings(Long id, Member member, Schedule schedule, Long waitNum) {
        this.id = id;
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

    public Long getWaitNum() {
        return waitNum;
    }

}
