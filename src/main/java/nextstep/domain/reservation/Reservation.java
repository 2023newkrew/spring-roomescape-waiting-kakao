package nextstep.domain.reservation;

import nextstep.domain.member.Member;
import nextstep.domain.schedule.Schedule;

import java.time.LocalDateTime;
import java.util.Objects;

import static nextstep.domain.reservation.ReservationStatus.UNAPPROVED;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private ReservationStatus status;
    private int deposit;
    private LocalDateTime createdAt;

    public Reservation() {
    }

    public Reservation(Schedule schedule, Member member, int deposit) {
        this.schedule = schedule;
        this.member = member;
        this.status = UNAPPROVED;
        this.deposit = deposit;
        this.createdAt = LocalDateTime.now();
    }

    public Reservation(Long id, Schedule schedule, Member member, String status, int deposit) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.status = UNAPPROVED;
        this.deposit = deposit;
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

    public ReservationStatus getStatus() {
        return status;
    }

    public ReservationStatus getTransitionedStatus() {
        return status.getNextStatus();
    }

    public int getDeposit() {
        return deposit;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    public boolean isLessThanDepositPolicy(int depositPolicy) {
        return deposit < depositPolicy;
    }
}
