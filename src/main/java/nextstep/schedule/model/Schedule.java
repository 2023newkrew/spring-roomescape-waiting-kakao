package nextstep.schedule.model;

import lombok.Getter;
import lombok.ToString;
import nextstep.theme.model.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@ToString
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
}
