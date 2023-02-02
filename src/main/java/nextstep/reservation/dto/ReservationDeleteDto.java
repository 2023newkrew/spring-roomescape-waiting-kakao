package nextstep.reservation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ReservationDeleteDto {
    private final int deletedRowCount;
}
