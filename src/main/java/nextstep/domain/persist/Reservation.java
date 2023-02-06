package nextstep.domain.persist;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.domain.enumeration.ReservationStatus;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private ReservationStatus status;

    public Reservation(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
        this.status = ReservationStatus.NOT_APPROVED;
    }

    public Reservation(Long id, Schedule schedule, Member member, String status) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.status = ReservationStatus.of(status);
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }
}
