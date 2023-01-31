package nextstep.reservation.service;

import lombok.RequiredArgsConstructor;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.domain.ReservationEntity;
import nextstep.reservation.exception.ReservationErrorMessage;
import nextstep.reservation.exception.ReservationException;
import nextstep.reservation.repository.ReservationRepository;
import nextstep.schedule.domain.Schedule;
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

    @Transactional
    @Override
    public ReservationEntity create(Reservation reservation) {
        validateSchedule(reservation.getSchedule());

        return repository.insert(reservation.toEntity());
    }

    private void validateSchedule(Schedule schedule) {
        Long scheduleId = schedule.getId();
        if (Objects.isNull(scheduleService.getById(scheduleId))) {
            throw new ReservationException(ReservationErrorMessage.NOT_EXISTS);
        }
        if (repository.existsByScheduleId(scheduleId)) {
            throw new ReservationException(ReservationErrorMessage.CONFLICT);
        }
    }

    @Override
    public ReservationEntity getById(Long id) {
        return repository.getById(id);
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
