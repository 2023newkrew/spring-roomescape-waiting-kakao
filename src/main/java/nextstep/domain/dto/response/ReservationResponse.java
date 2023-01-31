package nextstep.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.domain.enumeration.ReservationStatus;
import nextstep.domain.persist.Reservation;
import nextstep.domain.persist.Schedule;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    private Long id;
    private Schedule schedule;
    private ReservationStatus status;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.schedule = reservation.getSchedule();
        this.status = reservation.getStatus();
    }
}
