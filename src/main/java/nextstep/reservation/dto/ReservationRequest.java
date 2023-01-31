package nextstep.reservation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ReservationRequest {

    private final Long scheduleId;

    ReservationRequest() {
        this(null);
    }
}
