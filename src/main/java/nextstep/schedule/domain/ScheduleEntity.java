package nextstep.schedule.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nextstep.theme.domain.ThemeEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@AllArgsConstructor
public class ScheduleEntity {

    @Getter
    @Setter
    private Long id;

    @Getter
    private final LocalDate date;

    @Getter
    private final LocalTime time;

    @Getter
    private final ThemeEntity theme;

    public Long getThemeId() {
        if (Objects.isNull(theme)) {
            return null;
        }

        return theme.getId();
    }
}
