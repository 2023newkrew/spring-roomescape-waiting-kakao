package roomescape.nextstep.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import roomescape.nextstep.member.Member;
import roomescape.nextstep.schedule.Schedule;

import java.util.Objects;

@Builder
@AllArgsConstructor
@Getter
public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private ReservationStatus status;

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    public static Reservation of(Long id, Reservation reservation) {
        return new Reservation(id, reservation.getSchedule(), reservation.getMember(), reservation.getStatus());
    }
}
