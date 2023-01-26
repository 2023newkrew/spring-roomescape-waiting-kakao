package nextstep.reservationwaiting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.reservation.ReservationRequest;

@RequiredArgsConstructor
@Getter
public class ReservationWaitingRequest {
    private final Long scheduleId;

    public ReservationRequest toReservationRequest() {
        return new ReservationRequest(scheduleId);
    }
}
