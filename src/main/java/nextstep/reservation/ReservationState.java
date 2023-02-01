package nextstep.reservation;

import java.util.Arrays;
import nextstep.exceptions.exception.InvalidReservationStateException;

public enum ReservationState {
    UN_APPROVE, APPROVE, CANCEL, CANCEL_WAIT, REJECT;

    public static ReservationState from(String state) {
        return Arrays.stream(ReservationState.values())
                .filter((s) -> s.toString().equalsIgnoreCase(state))
                .findFirst()
                .orElseThrow(InvalidReservationStateException::new);
    }
}
