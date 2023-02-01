package com.nextstep.interfaces.reservation.dtos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import com.nextstep.interfaces.member.dtos.MemberResponse;
import com.nextstep.interfaces.schedule.dtos.ScheduleResponse;

@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class ReservationResponse {

    private final Long id;

    private final MemberResponse member;

    private final ScheduleResponse schedule;
}
