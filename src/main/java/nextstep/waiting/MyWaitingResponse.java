package nextstep.waiting;

import nextstep.schedule.Schedule;

public class MyWaitingResponse {
    private final Long id;
    private final Schedule schedule;
    private final Long waitNum;

    private MyWaitingResponse(Long id, Schedule schedule, Long waitNum) {
        this.id = id;
        this.schedule = schedule;
        this.waitNum = waitNum;
    }

    public static MyWaitingResponse from(Waiting waiting) {
        return new MyWaitingResponse(waiting.getId(), waiting.getSchedule(), waiting.getWaitNum());
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Long getWaitNum() {
        return waitNum;
    }
}
