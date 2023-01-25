package nextstep.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.domain.persist.Reservation;
import nextstep.domain.persist.Schedule;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class ReservationResponse {
    private Long id;
    private Schedule schedule;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.schedule = reservation.getSchedule();
    }
}
