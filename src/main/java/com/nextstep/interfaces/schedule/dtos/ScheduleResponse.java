package com.nextstep.interfaces.schedule.dtos;

import com.nextstep.interfaces.theme.dtos.ThemeResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class ScheduleResponse {

    private final Long id;

    private final LocalDate date;

    private final LocalTime time;

    private final ThemeResponse theme;
}
