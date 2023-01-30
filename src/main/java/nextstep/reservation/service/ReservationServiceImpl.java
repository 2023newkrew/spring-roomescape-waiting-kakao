package nextstep.reservation.service;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.MemberEntity;
import nextstep.reservation.domain.ReservationEntity;
import nextstep.reservation.exception.ReservationErrorMessage;
import nextstep.reservation.exception.ReservationException;
import nextstep.reservation.repository.ReservationRepository;
import nextstep.schedule.domain.ScheduleEntity;
import nextstep.schedule.exception.ScheduleErrorMessage;
import nextstep.schedule.exception.ScheduleException;
import nextstep.schedule.service.ScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repository;

    private final ScheduleService scheduleService;

    @Transactional
    @Override
    public ReservationEntity create(ReservationEntity reservation) {
        validateSchedule(reservation.getScheduleId());

        return repository.insert(reservation);
    }

    private void validateSchedule(Long scheduleId) {
        ScheduleEntity scheduleEntity = scheduleService.getById(scheduleId);
        if (Objects.isNull(scheduleEntity)) {
            throw new ScheduleException(ScheduleErrorMessage.NOT_EXISTS);
        }
        if (repository.existsByScheduleId(scheduleId)) {
            throw new ReservationException(ReservationErrorMessage.CONFLICT);
        }
    }

    @Override
    public ReservationEntity getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public List<ReservationResponse> getByMemberId(Long memberId) {
        return repository.getByMemberId(memberId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean deleteById(Long memberId, Long id) {
        ReservationEntity reservation = repository.getById(id);
        validateReservation(reservation, memberId);

        return repository.deleteById(id);
    }

    private void validateReservation(ReservationEntity reservation, Long memberId) {
        if (Objects.isNull(reservation)) {
            throw new ReservationException(ReservationErrorMessage.NOT_EXISTS);
        }
        if (!memberId.equals(reservation.getMemberId())) {
            throw new ReservationException(ReservationErrorMessage.NOT_OWNER);
        }
    }
}
