package app.nextstep.domain;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;

    public Reservation() {

    }

    public Reservation(Long id, Schedule schedule, Member member) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
    }

    public Reservation(Schedule schedule, Member member) {
        this(null, schedule, member);
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

    public boolean isReservationOf(Long memberId) {
        return this.member.getId().equals(memberId);
    }
}
