package nextstep.schedule.mapper;

import nextstep.schedule.domain.Schedule;
import nextstep.schedule.dto.ScheduleRequest;
import nextstep.schedule.dto.ScheduleResponse;
import nextstep.schedule.dto.ScheduleSearchRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface ScheduleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "theme.id", source = "themeId")
    Schedule fromRequest(ScheduleRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "theme.id", source = "themeId")
    Schedule fromRequest(ScheduleSearchRequest request);

    ScheduleResponse toResponse(Schedule schedule);

    List<ScheduleResponse> toResponses(List<Schedule> schedules);
}