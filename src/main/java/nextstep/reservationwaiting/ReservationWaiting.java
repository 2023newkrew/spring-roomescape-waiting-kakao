package nextstep.reservationwaiting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.schedule.Schedule;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReservationWaiting {
    private Long id;
    private Schedule schedule;
    private Long memberId;
    private Long priority;

    public ReservationWaiting(Schedule schedule, Long memberId, Long priority) {
        this.schedule = schedule;
        this.memberId = memberId;
        this.priority = priority;
    }
}
