package nextstep.reservation;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.schedule.Schedule;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    private Long id;
    private Schedule schedule;
    private LocalDateTime createdDateTime;
    private ReservationState state;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.schedule = reservation.getSchedule();
        this.createdDateTime = reservation.getCreatedDateTime();
        this.state = reservation.getState();
    }

    public ReservationResponse(Reservation reservation, ReservationState state) {
        this.id = reservation.getId();
        this.schedule = reservation.getSchedule();
        this.createdDateTime = reservation.getCreatedDateTime();
        this.state = state;
    }

}
