package nextstep.reservation.domain;

import nextstep.member.domain.Member;
import nextstep.reservationwaiting.domain.ReservationWaiting;
import nextstep.schedule.domain.Schedule;

import java.util.Objects;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;

    public Reservation() {
    }

    public Reservation(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
    }

    public Reservation(Long id, Schedule schedule, Member member) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
    }

    public static Reservation of(ReservationWaiting reservationWaiting){
        return new Reservation(
                reservationWaiting.getSchedule(),
                reservationWaiting.getMember()
        );
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

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }
}
