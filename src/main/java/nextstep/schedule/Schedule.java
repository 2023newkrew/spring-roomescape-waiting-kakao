package nextstep.schedule;

import nextstep.theme.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Schedule {
    private Long id;
    private Theme theme;
    private LocalDate date;
    private LocalTime time;

    public Schedule() {
    }

    public Schedule(Long id, Theme theme, LocalDate date, LocalTime time) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Schedule(Theme theme, LocalDate date, LocalTime time) {
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

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", theme=" + theme +
                ", date=" + date +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(id, schedule.id) && Objects.equals(theme, schedule.theme) && Objects.equals(date, schedule.date) && Objects.equals(time, schedule.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, theme, date, time);
    }
}
