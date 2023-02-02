package com.nextstep.domains.waiting.dtos;

public class WaitingRequest {
    private final Long scheduleId;

    public WaitingRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}
