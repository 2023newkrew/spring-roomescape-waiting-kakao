package nextstep.waiting;

import nextstep.reservation.Reservation;

public class ReservationWaiting {
    public Reservation reservation;
    public Long waitingSeq;

    public ReservationWaiting(Reservation reservation, Long waitingSeq) {
        this.reservation = reservation;
        this.waitingSeq = waitingSeq;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public Long getWaitingSeq() {
        return waitingSeq;
    }

    public void decreaseWaitingSeq() {
        waitingSeq--;
        if (waitingSeq < 0) {
            throw new IllegalStateException();
        }
    }
}
