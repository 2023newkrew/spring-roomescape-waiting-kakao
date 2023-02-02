package com.nextstep.domains.reservation.dtos;

public class ReservationRequest {
    private Long scheduleId;

    public ReservationRequest() {
    }

    public ReservationRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}
