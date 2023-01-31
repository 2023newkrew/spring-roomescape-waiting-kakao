package nextstep.domain.dto;

import nextstep.domain.persist.Schedule;
import nextstep.domain.persist.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleRequest {
    private long themeId;
    private String date;
    private String time;

    public ScheduleRequest() {
    }

    public ScheduleRequest(long themeId, String date, String time) {
        this.themeId = themeId;
        this.date = date;
        this.time = time;
    }

    public long getThemeId() {
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
