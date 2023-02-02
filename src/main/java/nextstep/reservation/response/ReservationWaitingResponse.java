package nextstep.reservation.response;

import lombok.Getter;
import lombok.Setter;
import nextstep.schedule.Schedule;

@Getter
@Setter
public class ReservationWaitingResponse {

    private Long id;
    private Schedule schedule;
    private Long waitNum;
}
