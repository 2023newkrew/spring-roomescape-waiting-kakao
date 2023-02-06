package nextstep.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private ReservationStatus reservationStatus;

    public Reservation(Schedule schedule, Member member, ReservationStatus reservationStatus) {
        this.schedule = schedule;
        this.member = member;
        this.reservationStatus = reservationStatus;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }
}
