package nextstep.exception;

import nextstep.exception.message.ErrorMessage;

public class ReservationException extends BaseException {

    public ReservationException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
