package nextstep.waiting;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class ReservationWaiting {

    private Long id;
    private Schedule schedule;
    private Member member;
    private Long waitNum;

    public ReservationWaiting() {
    }

    public ReservationWaiting(Schedule schedule, Member member, Long waitNum) {
        this.schedule = schedule;
        this.member = member;
        this.waitNum = waitNum;
    }

    public ReservationWaiting(Long id, Schedule schedule, Member member, Long waitNum) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.waitNum = waitNum;
    }
}
