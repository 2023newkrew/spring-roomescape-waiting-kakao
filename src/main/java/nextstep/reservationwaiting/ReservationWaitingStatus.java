package nextstep.reservationwaiting;

import java.util.Arrays;
import nextstep.exception.InvalidFieldException;

public enum ReservationWaitingStatus {
    WAITING(1), IN_PROGRESS(2), FINISHED(3), DROPPED(4);
    private final int value;

    ReservationWaitingStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static ReservationWaitingStatus of(int value) {
        return Arrays.stream(ReservationWaitingStatus.values()).filter(status -> status.value == value).findFirst()
                .orElseThrow(
                        () -> new InvalidFieldException(String.valueOf(value), ReservationWaitingStatus.class)
                );
    }
}
