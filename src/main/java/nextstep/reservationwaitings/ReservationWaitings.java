package nextstep.reservationwaitings;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class ReservationWaitings {

    private Long id;
    private Member member;
    private Schedule schedule;
    private Long waitNum;

    public ReservationWaitings() {}

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
