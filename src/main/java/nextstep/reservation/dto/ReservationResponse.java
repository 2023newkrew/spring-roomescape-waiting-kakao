package nextstep.reservation.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nextstep.theme.dto.ThemeResponse;

import java.time.LocalDate;
import java.time.LocalTime;

@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class ReservationResponse {

    private final Long id;

    private final LocalDate date;

    private final LocalTime time;

    private final String name;

    private final ThemeResponse theme;
}
