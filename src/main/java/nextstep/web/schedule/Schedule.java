package nextstep.web.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.web.theme.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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
}
