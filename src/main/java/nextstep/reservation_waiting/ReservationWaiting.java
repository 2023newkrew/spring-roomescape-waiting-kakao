package nextstep.reservation_waiting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationWaiting {
    private Long id;
    private Schedule schedule;
    private Member member;

    public ReservationWaiting(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
    }
}
