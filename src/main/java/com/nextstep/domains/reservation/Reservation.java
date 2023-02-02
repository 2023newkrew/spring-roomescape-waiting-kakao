package com.nextstep.domains.reservation;

import com.nextstep.domains.reservation.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.nextstep.domains.member.Member;
import com.nextstep.domains.schedule.Schedule;

import java.util.Objects;

@AllArgsConstructor
public class Reservation {

    @Getter
    @Setter
    private Long id;

    @Getter
    private final Member member;

    @Getter
    private final Schedule schedule;

    @Getter
    private final ReservationStatus status;

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
