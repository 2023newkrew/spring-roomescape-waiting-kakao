package nextstep.waiting.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class WaitingRequest {

    private final Long scheduleId;

    public WaitingRequest() {
        this(null);
    }
}
