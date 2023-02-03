package app.nextstep.dto;

import app.nextstep.domain.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleResponse {
    private Long id;
    private ThemeResponse theme;
    private LocalDate date;
    private LocalTime time;

    public ScheduleResponse(Schedule schedule) {
        this.id = schedule.getId();;
        this.theme = new ThemeResponse(schedule.getTheme());
        this.date = schedule.getDate();
        this.time = schedule.getTime();
    }

    public Long getId() {
        return id;
    }

    public ThemeResponse getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }
}
