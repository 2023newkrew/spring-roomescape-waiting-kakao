package nextstep.reservation_waiting;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class ReservationWaitingRequest {
    private final Long scheduleId;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ReservationWaitingRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }
}
