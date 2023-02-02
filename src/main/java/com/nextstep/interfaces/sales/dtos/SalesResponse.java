package com.nextstep.interfaces.sales.dtos;

import com.nextstep.domains.sales.enums.SalesStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class SalesResponse {
    private final Long id;

    private final Long reservationId;

    private final Integer amount;

    private final SalesStatus status;
}
