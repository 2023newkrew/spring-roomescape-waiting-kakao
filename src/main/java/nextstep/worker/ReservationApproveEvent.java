package nextstep.worker;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationApproveEvent {
    private Boolean isApprove;

    public Boolean isApproveTrue() {
        return isApprove;
    }
}
