package nextstep.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

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

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Member getMember() {
        return member;
    }

    @JsonIgnore
    public Long getScheduleId() {
        return schedule.getId();
    }

    public boolean checkMemberIsOwner(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", schedule=" + schedule +
                ", member=" + member +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(schedule, that.schedule) && Objects.equals(member, that.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, schedule, member);
    }
}
