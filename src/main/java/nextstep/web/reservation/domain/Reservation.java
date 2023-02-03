package nextstep.web.reservation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.web.member.domain.Member;
import nextstep.web.reservation_waiting.domain.ReservationWaiting;
import nextstep.web.schedule.domain.Schedule;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;

    public Reservation(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    public static Reservation fromReservationWaiting(ReservationWaiting reservationWaiting) {
        return new Reservation(
                reservationWaiting.getSchedule(),
                reservationWaiting.getMember()
        );
    }
}
