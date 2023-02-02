package nextstep.reservation;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationStatusHistory {

    private Long id;
    private Long reservationId;
    private ReservationStatus beforeStatus;
    private ReservationStatus afterStatus;
    private LocalDateTime changed_datetime;

    public ReservationStatusHistory(Reservation reservation, ReservationStatus afterStatus) {
        this.reservationId = reservation.getId();
        this.beforeStatus = reservation.getStatus();
        this.afterStatus = afterStatus;
    }

}
