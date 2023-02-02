package nextstep.reservation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ReservationCreateDto {
    private final boolean isReserved;
    private final Long id;
}
