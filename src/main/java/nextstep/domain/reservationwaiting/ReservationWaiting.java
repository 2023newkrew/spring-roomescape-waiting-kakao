package nextstep.domain.reservationwaiting;

import nextstep.domain.member.Member;
import nextstep.domain.schedule.Schedule;

public class ReservationWaiting {

    private Long id;
    private Member member;
    private Schedule schedule;
    private int waitNum;

    public ReservationWaiting(Long id, Member member, Schedule schedule, int waitNum) {
        this.id = id;
        this.member = member;
        this.schedule = schedule;
        this.waitNum = waitNum;
    }

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

    public boolean sameMember(Long memberId){
        return member.hasSameId(memberId);
    }
}
