package nextstep.waiting.mapper;

import nextstep.waiting.domain.WaitingEntity;
import nextstep.waiting.dto.WaitingResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface WaitingMapper {

    WaitingResponse toResponse(WaitingEntity waiting);
}