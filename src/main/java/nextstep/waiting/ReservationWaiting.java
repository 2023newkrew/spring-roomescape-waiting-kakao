package nextstep.waiting;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    public Long getReservationId() {
        return reservation.getId();
    }

    @JsonIgnore
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

    @Override
    public String toString() {
        return "ReservationWaiting{" +
                "reservation=" + reservation +
                ", waitingSeq=" + waitingSeq +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationWaiting that = (ReservationWaiting) o;
        return Objects.equals(reservation, that.reservation) && Objects.equals(waitingSeq, that.waitingSeq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservation, waitingSeq);
    }
}
