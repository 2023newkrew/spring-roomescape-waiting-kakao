package nextstep.reservationwaiting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.schedule.Schedule;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public class ReservationWaiting {
    private final Long id;
    private final Schedule schedule;
    private final Member member;
    private final Long waitNum;

    public ReservationWaiting(Schedule schedule, Member member) {
        this(null, schedule, member, null);
    }

    public Reservation toReservation() {
        return new Reservation(id, schedule, member);
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }
}
