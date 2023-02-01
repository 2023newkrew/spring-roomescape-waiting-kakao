package com.nextstep.domains.schedule;

import com.nextstep.domains.theme.Theme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@AllArgsConstructor
public class Schedule {

    @Getter
    @Setter
    private Long id;

    @Getter
    private final LocalDate date;

    @Getter
    private final LocalTime time;

    @Getter
    private final Theme theme;

    public Long getThemeId() {
        if (Objects.isNull(theme)) {
            return null;
        }

        return theme.getId();
    }
}
