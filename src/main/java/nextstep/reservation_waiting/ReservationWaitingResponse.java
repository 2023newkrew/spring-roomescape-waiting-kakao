package nextstep.reservation_waiting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextstep.schedule.Schedule;

@Getter
@Builder
@AllArgsConstructor
public class ReservationWaitingResponse {
    private final Long id;

    private final Schedule schedule;

    private final Long waitNum;
}
