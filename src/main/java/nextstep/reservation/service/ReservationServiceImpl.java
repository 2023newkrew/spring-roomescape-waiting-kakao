package nextstep.reservation.service;

import lombok.RequiredArgsConstructor;
import nextstep.etc.exception.ErrorMessage;
import nextstep.etc.exception.ReservationException;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;
import nextstep.reservation.mapper.ReservationMapper;
import nextstep.reservation.repository.ReservationRepository;
import nextstep.schedule.dto.ScheduleResponse;
import nextstep.schedule.service.ScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repository;

    private final ScheduleService scheduleService;

    private final ReservationMapper mapper;

    @Transactional
    @Override
    public ReservationResponse create(Long memberId, ReservationRequest request) {
        ScheduleResponse schedule = scheduleService.getById(request.getScheduleId());
        validateSchedule(schedule);
        Reservation reservation = mapper.fromRequest(memberId, request);

        return mapper.toResponse(repository.insert(reservation));
    }

    private void validateSchedule(ScheduleResponse schedule) {
        if (Objects.isNull(schedule)) {
            throw new ReservationException(ErrorMessage.SCHEDULE_NOT_EXISTS);
        }
        if (repository.existsByScheduleId(schedule.getId())) {
            throw new ReservationException(ErrorMessage.RESERVATION_CONFLICT);
        }
    }

    @Override
    public ReservationResponse getById(Long id) {
        Reservation reservation = repository.getById(id);

        return mapper.toResponse(reservation);
    }

    @Transactional
    @Override
    public boolean deleteById(Long memberId, Long id) {
        Reservation reservation = repository.getById(id);
        validateReservation(reservation, memberId);

        return repository.deleteById(id);
    }

    private void validateReservation(Reservation reservation, Long memberId) {
        if (Objects.isNull(reservation)) {
            throw new ReservationException(ErrorMessage.RESERVATION_NOT_EXISTS);
        }
        if (!memberId.equals(reservation.getMemberId())) {
            throw new ReservationException(ErrorMessage.NOT_RESERVER);
        }
    }
}
