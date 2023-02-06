package nextstep.waiting.mapper;

import nextstep.waiting.domain.Waiting;
import nextstep.waiting.dto.WaitingResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface WaitingMapper {

    WaitingResponse toResponse(Waiting waiting);
    
    List<WaitingResponse> toResponses(List<Waiting> waitings);
}