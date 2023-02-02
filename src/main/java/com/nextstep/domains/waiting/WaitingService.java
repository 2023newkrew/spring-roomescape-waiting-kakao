package com.nextstep.domains.waiting;

import com.nextstep.interfaces.exceptions.ErrorMessageType;
import lombok.RequiredArgsConstructor;
import com.nextstep.interfaces.exceptions.WaitingException;
import com.nextstep.interfaces.waiting.dtos.WaitingRequest;
import com.nextstep.interfaces.waiting.dtos.WaitingResponse;
import com.nextstep.interfaces.waiting.dtos.WaitingMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class WaitingService {

    private final WaitingRepository repository;

    private final WaitingMapper mapper;


    @Transactional
    public WaitingResponse create(Long memberId, WaitingRequest request) {
        Waiting waiting = mapper.fromRequest(memberId, request);

        return mapper.toResponse(tryInsert(waiting));
    }

    private Waiting tryInsert(Waiting waiting) {
        try {
            return repository.insert(waiting);
        }
        catch (DataIntegrityViolationException ignore) {
            throw new WaitingException(ErrorMessageType.WAITING_CONFLICT);
        }
    }

    public WaitingResponse getById(Long id) {
        return mapper.toResponse(repository.getById(id));
    }


    public Waiting getFirstByScheduleId(Long reservationId) {
        return repository.getFirstByReservationId(reservationId);
    }


    public List<WaitingResponse> getByMemberId(Long memberId) {
        return repository.getByMemberId(memberId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteById(Long memberId, Long id) {
        Waiting waiting = repository.getById(id);
        validateWaiting(waiting, memberId);

        return repository.deleteById(id);
    }

    private void validateWaiting(Waiting waiting, Long memberId) {
        if (Objects.isNull(waiting)) {
            throw new WaitingException(ErrorMessageType.WAITING_NOT_EXISTS);
        }
        if (!memberId.equals(waiting.getMemberId())) {
            throw new WaitingException(ErrorMessageType.NOT_WAITING_OWNER);
        }
    }
}
