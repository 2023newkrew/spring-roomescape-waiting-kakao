package nextstep.domain.reservation;

public class ReservationProjection {

    private Long id;
    private ReservationStatus status;
    private int deposit;

    public ReservationProjection(Long id, String status, int deposit) {
        this.id = id;
        this.status = ReservationStatus.valueOf(status);
        this.deposit = deposit;
    }

    public Long getId() {
        return id;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public int getDeposit() {
        return deposit;
    }
}
