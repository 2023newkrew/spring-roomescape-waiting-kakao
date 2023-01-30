package nextstep.waiting.service;

import lombok.RequiredArgsConstructor;
import nextstep.etc.exception.ErrorMessage;
import nextstep.etc.exception.ReservationException;
import nextstep.etc.exception.WaitingException;
import nextstep.reservation.repository.ReservationRepository;
import nextstep.schedule.repository.ScheduleRepository;
import nextstep.waiting.domain.Waiting;
import nextstep.waiting.dto.WaitingRequest;
import nextstep.waiting.dto.WaitingResponse;
import nextstep.waiting.mapper.WaitingMapper;
import nextstep.waiting.repository.WaitingRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class WaitingServiceImpl implements WaitingService {

    private final WaitingRepository repository;

    private final ScheduleRepository scheduleRepository;

    private final ReservationRepository reservationRepository;

    private final WaitingMapper mapper;


    @Transactional
    @Override
    public WaitingResponse create(Long memberId, WaitingRequest request) {
        Waiting waiting = mapper.fromRequest(memberId, request);

        return mapper.toResponse(tryInsert(waiting));
    }

    private Waiting tryInsert(Waiting waiting) {
        if (reservationRepository.existsByMemberIdAndScheduleId(waiting.getMemberId(), waiting.getScheduleId())) {
            throw new ReservationException(ErrorMessage.RESERVATION_CONFLICT);
        }
        try {
            return repository.insert(waiting);
        }
        catch (DataIntegrityViolationException ignore) {
            throw new WaitingException(ErrorMessage.WAITING_CONFLICT);
        }
    }

    @Override
    public WaitingResponse getById(Long id) {
        return null;
    }

    @Override
    public List<WaitingResponse> getByMemberId(Long memberId) {
        return null;
    }

    @Transactional
    @Override
    public boolean deleteById(Long MemberId, Long id) {
        return false;
    }
}
