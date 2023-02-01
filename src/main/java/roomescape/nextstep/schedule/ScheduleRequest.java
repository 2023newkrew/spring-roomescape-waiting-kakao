package roomescape.nextstep.schedule;

import roomescape.nextstep.theme.Theme;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleRequest {
    @PositiveOrZero
    private Long themeId;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String date;
    @Pattern(regexp = "\\d{2}:\\d{2}")
    private String time;

    public ScheduleRequest() {
    }

    public ScheduleRequest(Long themeId, String date, String time) {
        this.themeId = themeId;
        this.date = date;
        this.time = time;
    }

    public Long getThemeId() {
        return themeId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Schedule toEntity(Theme theme) {
        return new Schedule(
                theme,
                LocalDate.parse(this.date),
                LocalTime.parse(this.time)
        );
    }
}
