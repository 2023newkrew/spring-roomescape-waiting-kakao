package nextstep.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.domain.persist.Reservation;
import nextstep.domain.persist.Schedule;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    private long id;
    private Schedule schedule;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.schedule = reservation.getSchedule();
    }
}
