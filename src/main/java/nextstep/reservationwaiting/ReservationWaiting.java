package nextstep.reservationwaiting;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class ReservationWaiting {
    private Long id;
    private Schedule schedule;
    private Long memberId;
    private Long priority;

    public ReservationWaiting() {
    }

    public ReservationWaiting(Schedule schedule, Long memberId, Long priority) {
        this.schedule = schedule;
        this.memberId = memberId;
        this.priority = priority;
    }

    public ReservationWaiting(Long id, Schedule schedule, Long memberId, Long priority) {
        this.id = id;
        this.schedule = schedule;
        this.memberId = memberId;
        this.priority = priority;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getPriority() {
        return priority;
    }
}
