package nextstep.schedule;

import nextstep.theme.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
    private Long id;
    private Theme theme;
    private LocalDate date;
    private LocalTime time;
    private Long nextWaitingNumber;

    public Schedule() {
    }

    public Schedule(Long id, Theme theme, LocalDate date, LocalTime time, Long nextWaitingNumber) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.nextWaitingNumber = nextWaitingNumber;
    }

    public Schedule(Theme theme, LocalDate date, LocalTime time) {
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.nextWaitingNumber = 1L;
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

    public Long getNextWaitingNumber() {
        return nextWaitingNumber;
    }

    public void increaseWaitingNumber() {
        nextWaitingNumber++;
    }
}
