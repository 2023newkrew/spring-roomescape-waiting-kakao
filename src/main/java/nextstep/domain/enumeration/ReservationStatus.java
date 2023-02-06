package nextstep.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ReservationStatus {
    NOT_APPROVED("not_approved"),
    APPROVED("approved"),
    WAIT_CANCEL("wait_cancel"),
    CANCELED("canceled"),
    REJECTED("rejected");

    private final String status;

    public static ReservationStatus of(String string) {
        return Arrays.stream(ReservationStatus.values())
                .filter(status -> status.getStatus().equals(string))
                .findAny()
                .orElseThrow(NoSuchFieldError::new);
    }
}
