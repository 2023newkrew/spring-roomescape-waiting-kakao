package roomescape.nextstep.schedule;

import roomescape.nextstep.theme.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleRequest(Long themeId, String date, String time) {
    public Schedule toEntity(Theme theme) {
        return new Schedule(
                theme,
                LocalDate.parse(this.date),
                LocalTime.parse(this.time)
        );
    }
}
