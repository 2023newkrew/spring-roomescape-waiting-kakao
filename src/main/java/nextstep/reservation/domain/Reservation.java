package nextstep.reservation.domain;

import java.util.Objects;
import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private ReservationStatus status;

    public Reservation() {
    }

    public Reservation(Schedule schedule, Member member) {
        this(null, schedule, member, ReservationStatus.UNAPPROVED);
    }

    public Reservation(Long id, Schedule schedule, Member member, ReservationStatus status) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Member getMember() {
        return member;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    public void approve() {
        checkApproved();
        checkCancelled();
        checkWaitingCancel();
        checkRefused();
        status = ReservationStatus.APPROVED;
        // TODO 매출 기록
    }

    public void cancel() {
        checkCancelled();
        checkRefused();
        checkWaitingCancel();
        if (status == ReservationStatus.UNAPPROVED) {
            status = ReservationStatus.CANCELLED;
            return;
        }
        status = ReservationStatus.CANCEL_WAITING;
    }

    public void refuse() {
        checkRefused();
        checkCancelled();
        checkWaitingCancel();
        if (status == ReservationStatus.UNAPPROVED) {
            status = ReservationStatus.REFUSED;
            return;
        }
        status = ReservationStatus.REFUSED;
    }

    public void approveCancel() {
        checkApproved();
        checkRefused();
        checkCancelled();
        if (status == ReservationStatus.UNAPPROVED) {
            throw new RoomReservationException(ErrorCode.RESERVATION_CANT_BE_CANCELLED);
        }
        status = ReservationStatus.CANCELLED;
        // TODO 매출 기록 변경
    }

    private void checkApproved() {
        if (status == ReservationStatus.APPROVED) {
            throw new RoomReservationException(ErrorCode.RESERVATION_ALREADY_APPROVED);
        }
    }

    private void checkCancelled() {
        if (status == ReservationStatus.CANCELLED) {
            throw new RoomReservationException(ErrorCode.RESERVATION_ALREADY_CANCELLED);
        }
    }

    private void checkWaitingCancel() {
        if (status == ReservationStatus.CANCEL_WAITING) {
            throw new RoomReservationException(ErrorCode.RESERVATION_WAIT_CANCEL);
        }
    }

    private void checkRefused() {
        if (status == ReservationStatus.REFUSED) {
            throw new RoomReservationException(ErrorCode.RESERVATION_ALREADY_REFUSED);
        }
    }
}
