package nextstep.dto.response;

import nextstep.domain.schedule.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleResponse {

    private Long id;
    private ThemeResponse themeResponse;
    private LocalDate date;
    private LocalTime time;

    public ScheduleResponse(Schedule schedule) {
        this.id = schedule.getId();
        this.themeResponse = new ThemeResponse(schedule.getTheme());
        this.date = schedule.getDate();
        this.time = schedule.getTime();
    }

    public ScheduleResponse(Long id, ThemeResponse themeResponse, LocalDate date, LocalTime time) {
        this.id = id;
        this.themeResponse = themeResponse;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public ThemeResponse getTheme() {
        return themeResponse;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }
}
