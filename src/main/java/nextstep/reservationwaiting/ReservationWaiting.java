package nextstep.reservationwaiting;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class ReservationWaiting {
    private Long id;
    private Schedule schedule;
    private Member member;
    private Long waitingNum;
    private LocalDateTime regTime;

    public ReservationWaiting() {
    }

    public ReservationWaiting(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
        this.regTime = regTime;
    }

    public ReservationWaiting(Long id, Schedule schedule, Member member, LocalDateTime regTime) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.regTime = regTime;
    }

    public ReservationWaiting(Long id, Schedule schedule, Member member, Long waitingNum, LocalDateTime regTime) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.waitingNum = waitingNum;
        this.regTime = regTime;
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

    public LocalDateTime getRegTime() {
        return regTime;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }
}
