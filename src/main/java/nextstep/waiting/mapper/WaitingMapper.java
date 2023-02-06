package nextstep.waiting.mapper;

import nextstep.waiting.domain.Waiting;
import nextstep.waiting.dto.WaitingResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface WaitingMapper {

    WaitingResponse toResponse(Waiting waiting);
}