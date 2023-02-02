package nextstep.reservationwaiting;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

public class ReservationWaiting {
    private Long id;
    private Schedule schedule;
    private Member member;
    private ReservationWaitingStatus status;
    private Long waitingNum;

    public ReservationWaiting() {
    }

    public ReservationWaiting(Schedule schedule, Member member, ReservationWaitingStatus status) {
        this.schedule = schedule;
        this.member = member;
        this.status = status;
    }

    public ReservationWaiting(Long id, Schedule schedule, Member member, ReservationWaitingStatus status) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.status = status;
    }

    public ReservationWaiting(Long id, Schedule schedule, Member member, ReservationWaitingStatus status, Long waitingNum) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.status = status;
        this.waitingNum = waitingNum;
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

    public ReservationWaitingStatus getStatus() {
        return status;
    }

    public Long getWaitingNum() {
        return waitingNum;
    }

    public boolean isReservedBy(Member member) {
        return Objects.equals(this.member.getId(), member.getId());
    }
}
