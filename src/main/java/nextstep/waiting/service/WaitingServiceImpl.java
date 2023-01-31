package nextstep.waiting.service;

import lombok.RequiredArgsConstructor;
import nextstep.etc.exception.ErrorMessage;
import nextstep.etc.exception.WaitingException;
import nextstep.waiting.domain.Waiting;
import nextstep.waiting.dto.WaitingRequest;
import nextstep.waiting.dto.WaitingResponse;
import nextstep.waiting.mapper.WaitingMapper;
import nextstep.waiting.repository.WaitingRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class WaitingServiceImpl implements WaitingService {

    private final WaitingRepository repository;

    private final WaitingMapper mapper;


    @Transactional
    @Override
    public WaitingResponse create(Long memberId, WaitingRequest request) {
        Waiting waiting = mapper.fromRequest(memberId, request);

        return mapper.toResponse(tryInsert(waiting));
    }

    private Waiting tryInsert(Waiting waiting) {
        try {
            return repository.insert(waiting);
        }
        catch (DataIntegrityViolationException ignore) {
            throw new WaitingException(ErrorMessage.WAITING_CONFLICT);
        }
    }

    @Override
    public WaitingResponse getById(Long id) {
        return mapper.toResponse(repository.getById(id));
    }

    @Override
    public Waiting getFirstByScheduleId(Long reservationId) {
        return repository.getFirstByReservationId(reservationId);
    }

    @Override
    public List<WaitingResponse> getByMemberId(Long memberId) {
        return repository.getByMemberId(memberId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean deleteById(Long memberId, Long id) {
        Waiting waiting = repository.getById(id);
        validateWaiting(waiting, memberId);

        return repository.deleteById(id);
    }

    private void validateWaiting(Waiting waiting, Long memberId) {
        if (Objects.isNull(waiting)) {
            throw new WaitingException(ErrorMessage.WAITING_NOT_EXISTS);
        }
        if (!memberId.equals(waiting.getMemberId())) {
            throw new WaitingException(ErrorMessage.NOT_WAITING_OWNER);
        }
    }
}
