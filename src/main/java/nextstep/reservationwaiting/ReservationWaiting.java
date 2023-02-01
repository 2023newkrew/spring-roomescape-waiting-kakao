package nextstep.reservationwaiting;

import nextstep.schedule.Schedule;

public class ReservationWaiting {
    private Long id;
    private final Schedule schedule;
    private final Long memberId;
    private final Long waitNum;

    public ReservationWaiting(Schedule schedule, Long memberId, Long waitNum) {
        this(null, schedule, memberId, waitNum);
    }

    public ReservationWaiting(Long id, Schedule schedule, Long memberId, Long waitNum) {
        this.id = id;
        this.schedule = schedule;
        this.memberId = memberId;
        this.waitNum = waitNum;
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getWaitNum() {
        return waitNum;
    }
}
