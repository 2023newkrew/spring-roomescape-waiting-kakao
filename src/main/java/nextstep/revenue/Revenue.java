package nextstep.revenue;

import java.util.Optional;
import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;

public class Revenue {
    private Optional<Long> id;
    private Integer amount;
    private RevenueStatus status;

    public Revenue(Long id, Integer amount, RevenueStatus status) {
        this.id = Optional.ofNullable(id);
        this.amount = amount;
        this.status = status;
    }

    public Revenue(Integer amount) {
        this(null, amount, RevenueStatus.OK);
    }

    public Optional<Long> getId() {
        return id;
    }

    public Integer getAmount() {
        return amount;
    }

    public RevenueStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = Optional.ofNullable(id);
    }

    public void refund() {
        if (status == RevenueStatus.REFUND) {
            throw new RoomReservationException(ErrorCode.REVENUE_ALREADY_REFUND);
        }
        status = RevenueStatus.REFUND;
    }
}
