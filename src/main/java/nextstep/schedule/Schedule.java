package nextstep.schedule;

import nextstep.theme.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

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

    public static ScheduleBuilder builder() {
        return new ScheduleBuilder();
    }

    public static class ScheduleBuilder {
        private Long id;
        private Theme theme;
        private LocalDate date;
        private LocalTime time;

        private ScheduleBuilder() {
        }

        public ScheduleBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ScheduleBuilder theme(Theme theme) {
            this.theme = theme;
            return this;
        }

        public ScheduleBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public ScheduleBuilder time(LocalTime time) {
            this.time = time;
            return this;
        }

        public Schedule build() {
            return new Schedule(id, theme, date, time);
        }
    }
}
