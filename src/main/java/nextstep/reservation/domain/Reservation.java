package nextstep.reservation.domain;

import lombok.Value;
import nextstep.member.domain.Member;
import nextstep.schedule.domain.Schedule;

@Value
public class Reservation {
    Long id;
    Member member;
    Schedule schedule;

    public ReservationEntity toEntity() {
        return new ReservationEntity(id, member.toEntityWithRole(null), schedule.toEntity());
    }
}
