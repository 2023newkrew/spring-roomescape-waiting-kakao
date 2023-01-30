package nextstep.reservation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.schedule.Schedule;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReservationResponse {
    private Long id;
    private Schedule schedule;

    public static ReservationResponse of(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getSchedule());
    }
}
