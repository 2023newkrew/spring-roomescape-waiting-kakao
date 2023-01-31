package nextstep.reservation.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@NoArgsConstructor
public class ReservationWaitingRequest {
    @NotNull
    private Long scheduleId;

    public ReservationWaitingRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }
}
