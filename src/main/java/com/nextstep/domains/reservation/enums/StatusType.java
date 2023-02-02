package com.nextstep.domains.reservation.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StatusType {
    UNAPPROVED("예악 미승인"),
    APPROVED("예약 승인"),
    CANCELED("예약 취소"),
    CANCELED_WAIT("예약 취소 대기"),
    REJECTED("예약 거절");

    @Getter
    private final String type;

    public static StatusType typeOf(String name) {
        for (StatusType status : StatusType.values()) {
            if (status.getType().equals(name)) {
                return status;
            }
        }
        return null;
    }

}
