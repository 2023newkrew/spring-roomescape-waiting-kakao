package nextstep.waiting.service;

import lombok.RequiredArgsConstructor;
import nextstep.reservation.mapper.ReservationMapper;
import nextstep.reservation.repository.ReservationRepository;
import nextstep.schedule.repository.ScheduleRepository;
import nextstep.waiting.dto.WaitingRequest;
import nextstep.waiting.dto.WaitingResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class WaitingServiceImpl implements WaitingService {

    private final ReservationRepository repository;

    private final ScheduleRepository scheduleRepository;

    private final ReservationMapper mapper;


    @Override
    public WaitingResponse create(Long memberId, WaitingRequest request) {
        return null;
    }

    @Override
    public WaitingResponse getById(Long id) {
        return null;
    }

    @Override
    public List<WaitingResponse> getByMemberId(Long memberId) {
        return null;
    }

    @Override
    public boolean deleteById(Long MemberId, Long id) {
        return false;
    }
}
