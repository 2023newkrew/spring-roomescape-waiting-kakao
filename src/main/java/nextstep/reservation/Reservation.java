package nextstep.reservation;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

public class Reservation {

    private Long id;
    private Schedule schedule;
    private Member member;
    private Long waitTicketNumber;

    public Reservation() {
    }

    public Reservation(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
    }

    public Reservation(Long id, Schedule schedule, Member member, Long waitTicketNumber) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.waitTicketNumber = waitTicketNumber;
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

    public Long getWaitTicketNumber() {
        return waitTicketNumber;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }
}
