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
    private ReservationStatus status;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.schedule = reservation.getSchedule();
        this.createdDateTime = reservation.getCreatedDateTime();
        this.status = reservation.getStatus();
    }

    public ReservationResponse(Reservation reservation, ReservationStatus status) {
        this.id = reservation.getId();
        this.schedule = reservation.getSchedule();
        this.createdDateTime = reservation.getCreatedDateTime();
        this.status = status;
    }

}
