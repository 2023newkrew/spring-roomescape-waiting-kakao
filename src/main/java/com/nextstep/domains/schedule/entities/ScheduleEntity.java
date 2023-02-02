package com.nextstep.domains.schedule.entities;

import com.nextstep.domains.theme.entities.ThemeEntity;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleEntity {
    private Long id;
    private ThemeEntity theme;
    private LocalDate date;
    private LocalTime time;

    public ScheduleEntity() {
    }

    public ScheduleEntity(Long id, ThemeEntity theme, LocalDate date, LocalTime time) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public ScheduleEntity(ThemeEntity theme, LocalDate date, LocalTime time) {
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public ThemeEntity getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }
}
