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

    /* RestAssured에서 사용 */
    @SuppressWarnings("unused")
    public WaitingResponse() {
    }

    public long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    /* RestAssured에서 사용 */
    @SuppressWarnings("unused")
    public long getWaitNum() {
        return waitNum;
    }
}
