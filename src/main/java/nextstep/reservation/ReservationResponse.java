package nextstep.reservation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.schedule.Schedule;

@Getter
@NoArgsConstructor
public class ReservationResponse {
    private Long id;
    private Schedule schedule;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.schedule = reservation.getSchedule();
    }
}
