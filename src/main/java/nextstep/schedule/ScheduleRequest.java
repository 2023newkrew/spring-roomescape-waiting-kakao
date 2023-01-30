package nextstep.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import nextstep.theme.Theme;

public class ScheduleRequest {
    private final Long themeId;
    private final String date;
    private final String time;

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
