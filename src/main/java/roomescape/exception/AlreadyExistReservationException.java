package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "해당 시간에는 예약이 불가능합니다.")
public class AlreadyExistReservationException extends RuntimeException {
    private final LocalDate date;
    private final LocalTime time;

    public AlreadyExistReservationException(LocalDate date, LocalTime time) {
        this.date = date;
        this.time = time;
    }
}
