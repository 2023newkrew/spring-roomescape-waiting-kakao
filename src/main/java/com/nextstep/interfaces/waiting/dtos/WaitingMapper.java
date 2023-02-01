package com.nextstep.interfaces.waiting.dtos;

import com.nextstep.domains.waiting.Waiting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface WaitingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member.id", source = "memberId")
    @Mapping(target = "schedule.id", source = "request.scheduleId")
    Waiting fromRequest(Long memberId, WaitingRequest request);

    WaitingResponse toResponse(Waiting waiting);
}