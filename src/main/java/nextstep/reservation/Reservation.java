package nextstep.reservation;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private Long waitingSeq;

    public Reservation() {
    }

    public Reservation(Schedule schedule, Member member, Long waitingSeq) {
        this.schedule = schedule;
        this.member = member;
        this.waitingSeq = waitingSeq;
    }

    public Reservation(Long id, Schedule schedule, Member member, Long waitingSeq) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.waitingSeq = waitingSeq;
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

    public Long getWaitingSeq() {
        return waitingSeq;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    public ReservationResponse toResponse() {
        return new ReservationResponse(id, schedule, member);
    }
}
