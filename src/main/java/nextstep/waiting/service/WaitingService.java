package nextstep.waiting.service;

import nextstep.waiting.domain.Waiting;
import nextstep.waiting.dto.WaitingRequest;
import nextstep.waiting.dto.WaitingResponse;

import java.util.List;

public interface WaitingService {
    WaitingResponse create(Long memberId, WaitingRequest request);

    WaitingResponse getById(Long id);

    Waiting getFirstByScheduleId(Long scheduleId);

    List<WaitingResponse> getByMemberId(Long memberId);

    boolean deleteById(Long MemberId, Long id);
}
