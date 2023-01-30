package nextstep.reservationwaiting;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.reservation.ReservationRequest;

@RequiredArgsConstructor
@Getter
public class ReservationWaitingRequest {
    @JsonValue
    private final Long scheduleId;

    public ReservationRequest toReservationRequest() {
        return new ReservationRequest(scheduleId);
    }
}
