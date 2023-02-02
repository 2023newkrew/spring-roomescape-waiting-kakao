package com.nextstep.domains.waiting.dtos;

public class WaitingResponse {
    private final Long id;

    public WaitingResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
