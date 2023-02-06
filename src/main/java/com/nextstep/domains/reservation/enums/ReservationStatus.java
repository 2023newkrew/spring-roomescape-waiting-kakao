package com.nextstep.domains.reservation.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ReservationStatus {
    UNAPPROVED("예악 미승인"),
    APPROVED("예약 승인"),
    CANCELED("예약 취소"),
    CANCELED_WAIT("예약 취소 대기"),
    REJECTED("예약 거절");

    @Getter
    private final String desc;

    public static ReservationStatus typeOf(String name) {
        for (ReservationStatus status : ReservationStatus.values()) {
            if (status.getDesc().equals(name)) {
                return status;
            }
        }
        return null;
    }

}
