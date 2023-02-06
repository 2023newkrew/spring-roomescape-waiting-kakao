package nextstep.domain.reservation;

import nextstep.error.ApplicationException;

import static nextstep.error.ErrorType.INTERNAL_SERVER_ERROR;

public enum ReservationStatus {

    UNAPPROVED,
    APPROVED,
    CANCEL_PENDING,
    CANCELED,
    REJECTED
    ;

    public ReservationStatus getNextStatus() {
        switch (this) {
            case UNAPPROVED: return APPROVED;
            case CANCEL_PENDING: return CANCELED;
            default: throw new ApplicationException(INTERNAL_SERVER_ERROR);
        }
    }
}
