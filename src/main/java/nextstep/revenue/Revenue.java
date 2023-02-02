package nextstep.revenue;

import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;

public class Revenue {
    private Long id;
    private Integer amount;
    private RevenueStatus status;

    public Revenue(Long id, Integer amount, RevenueStatus status) {
        this.id = id;
        this.amount = amount;
        this.status = status;
    }

    public Revenue(Integer amount) {
        this(null, amount, RevenueStatus.OK);
    }

    public Long getId() {
        return id;
    }

    public Integer getAmount() {
        return amount;
    }

    public RevenueStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void refund() {
        if (status == RevenueStatus.REFUND) {
            throw new RoomReservationException(ErrorCode.REVENUE_ALREADY_REFUND);
        }
        status = RevenueStatus.REFUND;
        System.out.println("리베뉴");
    }
}
