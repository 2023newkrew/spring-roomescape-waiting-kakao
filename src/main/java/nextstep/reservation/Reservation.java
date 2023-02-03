package nextstep.reservation;

import static nextstep.utils.Validator.checkFieldIsNull;

import java.util.Objects;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class Reservation {
    private Long id;
    private final Schedule schedule;
    private final Member member;

    private Reservation(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
        validateFields();
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

    public static ReservationBuilder builder() {
        return new ReservationBuilder();
    }

    public static Reservation giveId(Reservation reservation, Long id) {
        checkFieldIsNull(reservation, "reservation");
        checkFieldIsNull(id,"id");
        reservation.id = id;
        return reservation;
    }

    public static class ReservationBuilder {

        private Schedule schedule;
        private Member member;

        public ReservationBuilder schedule(Schedule schedule) {
            this.schedule = schedule;
            return this;
        }

        public ReservationBuilder member(Member member) {
            this.member = member;
            return this;
        }

        public Reservation build() {
            return new Reservation(schedule, member);

        }

    }

    private void validateFields() {
        checkFieldIsNull(schedule, "schedule");
        checkFieldIsNull(member, "member");
    }
}
