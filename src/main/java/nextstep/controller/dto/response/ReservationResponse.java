package nextstep.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.domain.Reservation;
import nextstep.domain.Schedule;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private long id;
    private Schedule schedule;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.schedule = reservation.getSchedule();
    }
}
