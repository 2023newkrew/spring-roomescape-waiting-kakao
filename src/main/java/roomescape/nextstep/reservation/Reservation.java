package roomescape.nextstep.reservation;

import roomescape.nextstep.member.Member;
import roomescape.nextstep.schedule.Schedule;

import java.util.Objects;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private ReservationStatus status;

    public Reservation() {
    }

    public Reservation(Schedule schedule, Member member, ReservationStatus status) {
        this.schedule = schedule;
        this.member = member;
        this.status = status;
    }

    public Reservation(Long id, Schedule schedule, Member member, ReservationStatus status) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.status = status;
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

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    public static Reservation of(Long id, Reservation reservation){
        return new Reservation(id, reservation.getSchedule(), reservation.getMember(), reservation.getStatus());
    }
}
