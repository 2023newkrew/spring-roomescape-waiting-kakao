package com.nextstep.interfaces.schedule.dtos;

import com.nextstep.domains.schedule.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface ScheduleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "theme.id", source = "themeId")
    Schedule fromRequest(ScheduleRequest request);

    ScheduleResponse toResponse(Schedule schedule);
}