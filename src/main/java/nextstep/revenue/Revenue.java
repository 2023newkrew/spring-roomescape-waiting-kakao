package nextstep.revenue;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Revenue {

    private Long id;
    private long reservationId;
    private long price;

    public Revenue(long reservationId, long price) {
        this.reservationId = reservationId;
        this.price = price;
    }
}
