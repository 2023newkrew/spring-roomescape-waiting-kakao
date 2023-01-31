package nextstep.waitings;

import nextstep.schedule.Schedule;

public class Waiting {
    private final long id;
    private final Schedule schedule;
    private final long memberId;

    public Waiting(final long id, final Schedule schedule, final long memberId) {
        this.id = id;
        this.schedule = schedule;
        this.memberId = memberId;
    }

    public long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public long getMemberId() {
        return memberId;
    }
}
