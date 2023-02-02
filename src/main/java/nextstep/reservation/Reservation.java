package nextstep.reservation;

import nextstep.member.Member;
import nextstep.schedule.Schedule;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Objects;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private ReservationState state = ReservationState.NOT_APPROVED;

    /* RestAssured에서 사용 */
    @SuppressWarnings("unused")
    public Reservation() {
    }

    public Reservation(Schedule schedule, Member member,ReservationState state) {
        this.schedule = schedule;
        this.member = member;
        this.state = state;
    }

    public Reservation(Long id, Schedule schedule, Member member, ReservationState state) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.state = state;
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

    public ReservationState getState() {
        return state;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    public static ReservationBuilder builder() {
        return new ReservationBuilder();
    }

    public static class ReservationBuilder {
        private Long id;
        private Schedule schedule;
        private Member member;
        private ReservationState state;

        private ReservationBuilder() {
        }

        public ReservationBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ReservationBuilder schedule(Schedule schedule) {
            this.schedule = schedule;
            return this;
        }

        public ReservationBuilder member(Member member) {
            this.member = member;
            return this;
        }

        public ReservationBuilder state(ReservationState state) {
            this.state = state;
            return this;
        }

        public Reservation build() {
            return new Reservation(id, schedule, member, state);
        }
    }
}
