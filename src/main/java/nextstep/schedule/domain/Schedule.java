package nextstep.schedule.domain;

import lombok.Value;
import nextstep.theme.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
public class Schedule {
    Long id;
    LocalDate date;
    LocalTime time;
    Theme theme;

    public ScheduleEntity toEntity() {
        return new ScheduleEntity(id, date, time, theme.toEntity());
    }
}
