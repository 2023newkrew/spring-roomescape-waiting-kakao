package nextstep.waiting;

import nextstep.member.Member;
import nextstep.reservation.Reservation;

import java.util.Objects;

public class ReservationWaiting {
    private Reservation reservation;
    private Long waitingSeq;

    public ReservationWaiting() {
    }

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

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public void setWaitingSeq(Long waitingSeq) {
        this.waitingSeq = waitingSeq;
    }

    public Long getScheduleId() {
        return reservation.getScheduleId();
    }

    public boolean checkMemberIsOwner(Member member) {
        return reservation.checkMemberIsOwner(member);
    }

    public void decreaseWaitingSeq() {
        waitingSeq--;
        if (waitingSeq < 0) {
            throw new IllegalStateException();
        }
    }
}
