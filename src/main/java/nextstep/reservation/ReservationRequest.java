package nextstep.reservation;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ReservationRequest {
    @JsonValue
    private final Long scheduleId;
}
