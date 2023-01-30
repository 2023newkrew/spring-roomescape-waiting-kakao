package nextstep.waiting;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class ReservationWaiting {
    private Long id;
    private Long waitingNumber;
    private Schedule schedule;
    @JsonIgnore
    private Member member;

    public ReservationWaiting(Long id, Schedule schedule, Member member, Long waitingNumber) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.waitingNumber = waitingNumber;
    }
}
