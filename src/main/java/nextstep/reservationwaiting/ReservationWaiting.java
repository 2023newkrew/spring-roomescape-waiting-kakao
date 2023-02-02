package nextstep.reservationwaiting;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.time.LocalDateTime;
import java.util.Objects;

public class ReservationWaiting {
    private Long id;
    private Schedule schedule;
    private Member member;
    private ReservationWaitingStatus status;
    private LocalDateTime createdAt;
    private Long waitingNum;

    public ReservationWaiting() {
    }

    public ReservationWaiting(Schedule schedule, Member member, ReservationWaitingStatus status, LocalDateTime createdAt) {
        this.schedule = schedule;
        this.member = member;
        this.status = status;
        this.createdAt = createdAt;
    }

    public ReservationWaiting(Long id, Schedule schedule, Member member, ReservationWaitingStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.status = status;
        this.createdAt = createdAt;
    }

    public ReservationWaiting(Long id, Schedule schedule, Member member, ReservationWaitingStatus status, LocalDateTime createdAt, Long waitingNum) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.status = status;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getWaitingNum() {
        return waitingNum;
    }

    public boolean isReservedBy(Member member) {
        return Objects.equals(this.member.getId(), member.getId());
    }
}
