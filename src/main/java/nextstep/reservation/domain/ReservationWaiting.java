package nextstep.reservation.domain;

import java.util.Objects;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class ReservationWaiting {
    private Long id;
    private Schedule schedule;
    private Member member;
    private Long waitingNum;

    public ReservationWaiting() {
    }

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

    public boolean isMine(Member member) {
        return Objects.equals(member.getId(), this.member.getId());
    }

    public Reservation convertToReservation() {
        return new Reservation(schedule, member);
    }
}
