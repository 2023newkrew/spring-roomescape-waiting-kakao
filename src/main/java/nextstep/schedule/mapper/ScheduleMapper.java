package nextstep.schedule.mapper;

import nextstep.schedule.domain.Schedule;
import nextstep.schedule.domain.ScheduleEntity;
import nextstep.schedule.dto.ScheduleRequest;
import nextstep.schedule.dto.ScheduleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface ScheduleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "theme.id", source = "themeId")
    Schedule fromRequest(ScheduleRequest request);

    ScheduleResponse toResponse(ScheduleEntity schedule);
}