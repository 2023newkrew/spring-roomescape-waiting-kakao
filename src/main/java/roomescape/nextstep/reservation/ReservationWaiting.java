package roomescape.nextstep.reservation;

public class ReservationWaiting extends Reservation {

    private Long waitNum;

    public ReservationWaiting() {
    }

    public ReservationWaiting(Reservation reservation, Long waitNum) {
        super(reservation.getId(), reservation.getSchedule(), reservation.getMember());
        this.waitNum = waitNum;
    }

    public Long getWaitNum() {
        return waitNum;
    }
}
