package roomwaiting.nextstep.schedule;

import org.h2.util.StringUtils;
import roomwaiting.nextstep.theme.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

import static roomwaiting.support.Messages.EMPTY_VALUE;

public class Schedule {
    private Long id;
    private Theme theme;
    private LocalDate date;
    private LocalTime time;

    public Schedule() {
    }

    public Schedule(Long id, Theme theme, LocalDate date, LocalTime time) {
        checkEmptyValue(date, time);
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Schedule(Theme theme, LocalDate date, LocalTime time) {
        checkEmptyValue(date, time);
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

    private void checkEmptyValue(LocalDate date, LocalTime time){
        if (StringUtils.isNullOrEmpty(String.valueOf(date))  || StringUtils.isNullOrEmpty(String.valueOf(time))) {
            throw new NullPointerException(EMPTY_VALUE.getMessage());
        }
    }
}
