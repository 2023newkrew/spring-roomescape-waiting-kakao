package nextstep.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Schedule {
    private Long id;
    private Theme theme;
    private LocalDate date;
    private LocalTime time;

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
