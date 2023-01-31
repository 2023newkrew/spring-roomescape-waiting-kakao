package nextstep.schedule;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.theme.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
public class ScheduleRequest {
    private final Long themeId;
    private final String date;
    private final String time;

    public Schedule toEntity(Theme theme) {
        return new Schedule(
                theme,
                LocalDate.parse(this.date),
                LocalTime.parse(this.time)
        );
    }
}
