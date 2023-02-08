package app.nextstep.domain;

public class ReservationWaiting extends Reservation{
    private Long waitingNumber;
    public ReservationWaiting(Long id, Schedule schedule, Member member, Long waitingNumber) {
        super(id, schedule, member, "WAITING");
        this.waitingNumber = waitingNumber;
    }

    public Long getWaitingNumber() {
        return waitingNumber;
    }
}
