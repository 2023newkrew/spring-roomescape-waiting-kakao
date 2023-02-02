package nextstep.reservation;

import nextstep.member.Member;
import nextstep.reservation_waiting.ReservationWaiting;
import nextstep.schedule.Schedule;

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

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Member getMember() {
        return member;
    }

    public boolean isSameMember(Member member){
        return this.member.equals(member);
    }

    public static Reservation fromReservationWaiting(ReservationWaiting reservationWaiting) {
        return new Reservation(
                reservationWaiting.getSchedule(),
                reservationWaiting.getMember()
        );
    }
}
