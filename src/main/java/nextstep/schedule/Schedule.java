package nextstep.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import nextstep.theme.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
    private Long id;
    private final Theme theme;
    private final LocalDate date;
    private final LocalTime time;

    @JsonCreator
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
}
