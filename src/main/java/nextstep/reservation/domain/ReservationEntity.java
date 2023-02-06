package nextstep.reservation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nextstep.member.domain.MemberEntity;
import nextstep.schedule.domain.ScheduleEntity;

import java.util.Objects;

@AllArgsConstructor
public class ReservationEntity {

    @Getter
    @Setter
    private Long id;

    @Getter
    private final MemberEntity member;

    @Getter
    private final ScheduleEntity schedule;

    public Long getMemberId() {
        if (Objects.isNull(member)) {
            return null;
        }

        return member.getId();
    }

    public Long getScheduleId() {
        if (Objects.isNull(schedule)) {
            return null;
        }

        return schedule.getId();
    }
}
