package app.nextstep.dto;

import app.nextstep.domain.Schedule;
import app.nextstep.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleRequest {
    private Long themeId;
    private String date;
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

    public Schedule toSchedule() {
        return new Schedule(
                new Theme(themeId),
                LocalDate.parse(date),
                LocalTime.parse(time));
    }
}
