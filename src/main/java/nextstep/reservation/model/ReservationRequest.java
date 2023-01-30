package nextstep.reservation.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@NoArgsConstructor
public class ReservationRequest {
    @NotNull
    private Long scheduleId;

    public ReservationRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }
}
