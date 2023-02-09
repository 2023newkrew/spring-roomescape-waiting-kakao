package nextstep.reservation;

import java.util.Arrays;
import nextstep.exceptions.exception.InvalidReservationStatusException;
import nextstep.exceptions.exception.ReservationStatusException;

public enum ReservationStatus implements ReservationStatusTransit {
    UN_APPROVE() {
        @Override
        public ReservationStatus transitStatus(ReservationStatus status, String role) {
            if (status == APPROVE && !role.equalsIgnoreCase("admin")) {
                throw new ReservationStatusException("관리자만이 예약 승인을 할 수 있습니다.");
            }
            if (status == REJECT && !role.equalsIgnoreCase("admin")) {
                throw new ReservationStatusException("관리자만이 예약 거절을 할 수 있습니다.");
            }
            if (status == APPROVE || status == REJECT || status == CANCEL) {
                return status;
            }
            throw new ReservationStatusException(String.format("%s 상태에서 %s 상태로 변경할 수 없습니다.", this.toString(), status.toString()));
        }
    }, APPROVE {
        @Override
        public ReservationStatus transitStatus(ReservationStatus status, String role) {
            if (status == REJECT && !role.equalsIgnoreCase("admin")) {
                throw new ReservationStatusException("관리자만이 예약 승인을 할 수 있습니다.");
            }
            if (status == REJECT || status == CANCEL_WAIT) {
                return status;
            }
            throw new ReservationStatusException(String.format("%s 상태에서 %s 상태로 변경할 수 없습니다.", this.toString(), status.toString()));
        }
    }, CANCEL {
        @Override
        public ReservationStatus transitStatus(ReservationStatus status, String role) {
            throw new ReservationStatusException("취소 상태에선 상태를 변경할 수 없습니다.");
        }
    }, CANCEL_WAIT {
        @Override
        public ReservationStatus transitStatus(ReservationStatus status, String role) {
            if (status == ReservationStatus.CANCEL && !role.equalsIgnoreCase("admin")) {
                throw new ReservationStatusException("관리자만이 예약 취소를 승인할 수 있습니다.");
            }
            if (status == ReservationStatus.CANCEL) {
                return status;
            }
            throw new ReservationStatusException(String.format("%s 상태에서 %s 상태로 변경할 수 없습니다.", this.toString(), status.toString()));

        }
    }, REJECT {
        @Override
        public ReservationStatus transitStatus(ReservationStatus status, String role) {
            throw new ReservationStatusException("거절 상태에선 상태를 변경할 수 없습니다.");
        }
    };

    public static ReservationStatus from(String status) {
        return Arrays.stream(values())
                .filter(reservationStatus -> reservationStatus.toString().equals(status))
                .findFirst()
                .orElseThrow(InvalidReservationStatusException::new);
    }

}
