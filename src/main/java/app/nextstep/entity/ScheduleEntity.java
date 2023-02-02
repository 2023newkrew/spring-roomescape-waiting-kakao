package app.nextstep.entity;

import app.nextstep.domain.Schedule;

import java.sql.Date;
import java.sql.Time;


public class ScheduleEntity {
    private Long id;
    private ThemeEntity theme;
    private Date date;
    private Time time;

    public ScheduleEntity(Long id, ThemeEntity theme, Date date, Time time) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Schedule toSchedule() {
        return new Schedule(id, theme.toTheme(), date.toLocalDate(), time.toLocalTime());
    }
}
