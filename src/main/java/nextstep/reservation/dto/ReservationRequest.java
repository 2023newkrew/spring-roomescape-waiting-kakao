package nextstep.reservation.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class ReservationRequest {

    private final Long scheduleId;

    ReservationRequest() {
        this(null);
    }
}
