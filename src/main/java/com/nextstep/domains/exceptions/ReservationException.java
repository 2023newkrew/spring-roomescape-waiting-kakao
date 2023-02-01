package com.nextstep.domains.exceptions;

public class ReservationException extends BaseException {

    public ReservationException(ErrorMessageType errorMessageType) {
        super(errorMessageType);
    }
}
