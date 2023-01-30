package nextstep.dto.response;

import nextstep.domain.schedule.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleResponse {

    private Long id;
    private ThemeResponse themeResponse;
    private LocalDate date;
    private LocalTime time;

    public ScheduleResponse() {
    }

    public ScheduleResponse(Schedule schedule) {
        this.id = schedule.getId();
        this.themeResponse = new ThemeResponse(schedule.getTheme());
        this.date = schedule.getDate();
        this.time = schedule.getTime();
    }

    public Long getId() {
        return id;
    }

    public ThemeResponse getThemeResponse() {
        return themeResponse;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }
}
