package nextstep.schedule;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nextstep.theme.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
public class Schedule {
    private final Long id;
    @NonNull
    private final Theme theme;
    @NonNull
    private final LocalDate date;
    @NonNull
    private final LocalTime time;

    public Schedule(Theme theme, LocalDate date, LocalTime time) {
        this(null, theme, date, time);
    }
}
