package com.nextstep.domains.sales.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SalesStatus {
    APPROVE("매출 승인"), CANCEL("매출 철회");
    @Getter
    private final String desc;

}
