package nextstep.domain.persist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
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
}
