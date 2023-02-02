package com.nextstep.interfaces.sales.dtos;

import com.nextstep.domains.sales.Sales;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface SalesMapper {
    SalesResponse toResponse(Sales sales);
}
