package nextstep.schedule.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;

public class ScheduleResponse {

    private final Long id;
    private final Theme theme;
    private final LocalDate date;
    private final LocalTime time;

    private ScheduleResponse(Long id, Theme theme, LocalDate date, LocalTime time) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public static ScheduleResponse of(Schedule schedule) {
        return new ScheduleResponse(schedule.getId(), schedule.getTheme(), schedule.getDate(), schedule.getTime());
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
}
