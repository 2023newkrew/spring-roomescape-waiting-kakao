package nextstep.schedule;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.theme.ThemeResponse;

import java.time.LocalDate;
import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
public class ScheduleResponse {
    private final Long id;
    private final ThemeResponse themeResponse;
    private final LocalDate date;
    private final LocalTime time;

    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(schedule.getId(), ThemeResponse.from(schedule.getTheme()), schedule.getDate(), schedule.getTime());
    }
}
