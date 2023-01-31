package nextstep.schedule;

import static nextstep.utils.Validator.checkFieldIsNull;

import java.time.LocalDate;
import java.time.LocalTime;
import nextstep.theme.Theme;

public class Schedule {
    private Long id;
    private final Theme theme;
    private final LocalDate date;
    private final LocalTime time;

    private Schedule(Theme theme, LocalDate date, LocalTime time) {
        this.theme = theme;
        this.date = date;
        this.time = time;
        validateFields();
    }

    public static Schedule giveId(Schedule schedule, Long id){
        checkFieldIsNull(id, "id");
        schedule.id = id;
        return schedule;
    }

    public static ScheduleBuilder builder() {
        return new ScheduleBuilder();
    }

    public static class ScheduleBuilder {

        private Theme theme;
        private LocalDate date;
        private LocalTime time;
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
            return new Schedule(theme, date, time);
        }

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

    private void validateFields() {
        checkFieldIsNull(theme, "theme");
        checkFieldIsNull(date, "date");
        checkFieldIsNull(time, "time");
    }
}
