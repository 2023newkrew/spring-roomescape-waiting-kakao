package nextstep.reservation.response;

import lombok.Getter;
import lombok.Setter;
import nextstep.schedule.Schedule;

@Getter
@Setter
public class ReservationResponse {
    private Long id;
    private Schedule schedule;
}
