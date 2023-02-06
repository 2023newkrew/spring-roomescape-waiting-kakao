package nextstep.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.domain.enumeration.ReservationStatus;
import nextstep.domain.persist.Reservation;
import nextstep.domain.persist.Schedule;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    @Schema(description = "예약 id")
    private Long id;
    @Schema(description = "예약된 스케줄")
    private Schedule schedule;
    @Schema(description = "예약 상태")
    private ReservationStatus status;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.schedule = reservation.getSchedule();
        this.status = reservation.getStatus();
    }
}
