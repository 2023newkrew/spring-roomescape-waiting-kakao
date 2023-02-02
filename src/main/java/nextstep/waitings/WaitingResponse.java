package nextstep.waitings;

import nextstep.schedule.Schedule;

public class WaitingResponse {
    private long id;
    private Schedule schedule;
    private long waitNum;

    public WaitingResponse(final Waiting waiting, final long waitNum) {
        this.id = waiting.getId();
        this.schedule = waiting.getSchedule();
        this.waitNum = waitNum;
    }

    public WaitingResponse() {
    }

    public long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public long getWaitNum() {
        return waitNum;
    }
}
