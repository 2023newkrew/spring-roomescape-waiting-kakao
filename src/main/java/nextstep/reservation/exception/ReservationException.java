package nextstep.reservation.exception;

import nextstep.exception.BaseException;

public class ReservationException extends BaseException {

    public ReservationException(ReservationErrorMessage errorMessage) {
        super(errorMessage);
    }
}
