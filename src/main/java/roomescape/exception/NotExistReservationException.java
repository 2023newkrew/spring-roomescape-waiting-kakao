package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "존재하지 않는 자원에 대한 요청입니다.")
public class NotExistReservationException extends RuntimeException {
    public final Long id;

    public NotExistReservationException(Long id) {
        this.id = id;
    }
}
