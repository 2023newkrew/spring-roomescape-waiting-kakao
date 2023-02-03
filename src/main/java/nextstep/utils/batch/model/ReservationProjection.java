package nextstep.utils.batch.model;

import nextstep.domain.reservation.ReservationStatus;

import static nextstep.domain.reservation.ReservationStatus.APPROVED;

public class ReservationProjection implements PagingModel {

    private Long id;
    private Long themeId;
    private ReservationStatus status;
    private int deposit;

    public ReservationProjection(Long id, Long themeId, String status, int deposit) {
        this.id = id;
        this.themeId = themeId;
        this.status = ReservationStatus.valueOf(status);
        this.deposit = deposit;
    }

    public ReservationProjection transitStatus() {
        this.status = this.status.getNextStatus();
        return this;
    }

    public Long getId() {
        return id;
    }

    public Long getThemeId() {
        return themeId;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public int getDeposit() {
        return deposit;
    }

    public boolean isApproved() {
        return status.equals(APPROVED);
    }
}
