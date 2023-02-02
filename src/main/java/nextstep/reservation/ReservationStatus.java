package nextstep.reservation;

import java.util.Arrays;
import nextstep.exceptions.exception.InvalidReservationStatusException;
import nextstep.exceptions.exception.ReservationStatusException;

public enum ReservationStatus {
    UN_APPROVE, APPROVE, CANCEL, CANCEL_WAIT, REJECT;

    public static ReservationStatus from(String status) {
        return Arrays.stream(ReservationStatus.values())
                .filter((s) -> s.toString().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(InvalidReservationStatusException::new);
    }

    public ReservationStatus changeToReject() {
        if (!this.equals(UN_APPROVE) && !this.equals(APPROVE)) {
            throw new ReservationStatusException("예약완료 혹은 예약대기 상태가 아니면 예약거절 상태로 변경할 수 없습니다.");
        }
        return REJECT;
    }

    public ReservationStatus changeToApprove() {
        if (!this.equals(UN_APPROVE)) {
            throw new ReservationStatusException("미승인 상태의 예약만 예약승인 상태로 변경할 수 있습니다.");
        }
        return APPROVE;
    }

    public ReservationStatus changeToCancelFromMember() {
        if (this.equals(UN_APPROVE)) {
            return CANCEL;
        }
        if (this.equals(APPROVE)) {
            return CANCEL_WAIT;
        }
        throw new ReservationStatusException("멤버는 승인 혹은 미승인 상태이어야 취소신청을 할 수 있습니다.");
    }

    public ReservationStatus changeToCancelFromAdmin() {
        if (!this.equals(CANCEL_WAIT)) {
            throw new ReservationStatusException("취소대기 상태가 아니라 취소 승인을 할 수 없습니다.");
        }
        return CANCEL;
    }
}
