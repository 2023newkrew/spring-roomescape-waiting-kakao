package nextstep.dto.request;

public class ReservationRequest {
    private Long scheduleId;
    private int deposit;

    public ReservationRequest(Long scheduleId, int deposit) {
        this.scheduleId = scheduleId;
        this.deposit = deposit;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public int getDeposit() {
        return deposit;
    }

    public boolean isLessThanDepositPolicy(int depositPolicy) {
        return deposit < depositPolicy;
    }
}
