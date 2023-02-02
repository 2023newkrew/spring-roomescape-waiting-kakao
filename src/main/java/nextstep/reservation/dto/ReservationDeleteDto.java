package nextstep.reservation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.reservation.response.ReservationCancelResponse;

@RequiredArgsConstructor
@Getter
public class ReservationDeleteDto {
    private final int deletedRowCount;

    public ReservationCancelResponse toReservationCancelResponse() {
        ReservationCancelResponse reservationCancelResponse = new ReservationCancelResponse();
        reservationCancelResponse.setDeletedReservationCount(deletedRowCount);
        return reservationCancelResponse;
    }
}
