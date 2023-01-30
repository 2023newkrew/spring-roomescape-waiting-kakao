package nextstep.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.schedule.Schedule;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReservationResponse {
    private Long id;
    private Schedule schedule;

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getSchedule());
    }
}
