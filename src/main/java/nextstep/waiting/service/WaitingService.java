package nextstep.waiting.service;

import nextstep.waiting.dto.WaitingRequest;
import nextstep.waiting.dto.WaitingResponse;

import java.util.List;

public interface WaitingService {
    WaitingResponse create(Long memberId, WaitingRequest request);

    WaitingResponse getById(Long id);

    List<WaitingResponse> getByMemberId(Long memberId);

    boolean deleteById(Long MemberId, Long id);
}
