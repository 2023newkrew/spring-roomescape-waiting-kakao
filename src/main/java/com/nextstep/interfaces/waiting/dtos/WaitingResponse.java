package com.nextstep.interfaces.waiting.dtos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import com.nextstep.interfaces.schedule.dtos.ScheduleResponse;

@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class WaitingResponse {

    private final Long id;

    private final ScheduleResponse schedule;

    private final Integer waitNum;
}
