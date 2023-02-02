package nextstep.schedule;

import nextstep.theme.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleResponse {
    private Long id;
    private Theme theme;
    private LocalDate date;
    private LocalTime time;

    public ScheduleResponse() {
    }

    public ScheduleResponse(Long id, Theme theme, LocalDate date, LocalTime time) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public ScheduleResponse(Theme theme, LocalDate date, LocalTime time) {
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public static ScheduleResponse fromEntity(Schedule schedule) {
        return new ScheduleResponse(schedule.getId(), schedule.getTheme(), schedule.getDate(), schedule.getTime());
    }
}
