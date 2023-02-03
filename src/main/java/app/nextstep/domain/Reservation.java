package app.nextstep.domain;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private String status;

    public Reservation(Long id, Schedule schedule, Member member, String status) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.status = status;
    }

    public Reservation(Schedule schedule, Member member) {
        this(null, schedule, member, null);
    }

    public Reservation(Long id, String status) {
        this(id, null, null, status);
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

    public String getStatus() {
        return status;
    }

    public void confirmed() {
        this.status = "CONFIRMED";
    }

    public void waiting() {
        this.status = "WAITING";
    }

    public boolean isConfirmed() {
        return this.status == "CONFIRMED";
    }

    public boolean isReservationOf(Long memberId) {
        return this.member.getId().equals(memberId);
    }
}
