package nextstep.schedule.dto;

import lombok.Value;
import nextstep.theme.dto.ThemeResponse;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
public class ScheduleResponse {
    Long id;
    LocalDate date;
    LocalTime time;
    ThemeResponse theme;
}
