package nextstep.reservation.service;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.exception.ReservationErrorMessage;
import nextstep.reservation.exception.ReservationException;
import nextstep.reservation.repository.ReservationRepository;
import nextstep.schedule.domain.Schedule;
import nextstep.schedule.exception.ScheduleErrorMessage;
import nextstep.schedule.exception.ScheduleException;
import nextstep.schedule.service.ScheduleService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repository;

    private final ScheduleService scheduleService;

    private final ApplicationEventPublisher publisher;

    @Transactional
    @Override
    public Reservation create(Reservation reservation) {
        validateSchedule(reservation.getScheduleId());

        return repository.insert(reservation);
    }

    private void validateSchedule(Long scheduleId) {
        Schedule schedule = scheduleService.getById(scheduleId);
        if (Objects.isNull(schedule)) {
            throw new ScheduleException(ScheduleErrorMessage.NOT_EXISTS);
        }
        if (repository.existsBySchedule(schedule)) {
            throw new ReservationException(ReservationErrorMessage.CONFLICT);
        }
    }

    @Override
    public Reservation getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public List<Reservation> getByMember(Member member) {
        return repository.getByMember(member);
    }

    @Transactional
    @Override
    public boolean deleteById(Member member, Long id) {
        Reservation reservation = getValidReservation(member, id);
        boolean deleted = repository.deleteById(id);
        if (deleted) {
            publisher.publishEvent(reservation);
        }

        return deleted;
    }

    private Reservation getValidReservation(Member member, Long id) {
        Reservation reservation = repository.getById(id);
        validateReservation(member, reservation);

        return reservation;
    }

    private void validateReservation(Member member, Reservation reservation) {
        if (Objects.isNull(reservation)) {
            throw new ReservationException(ReservationErrorMessage.NOT_EXISTS);
        }
        if (!Objects.equals(member.getId(), reservation.getMemberId())) {
            throw new ReservationException(ReservationErrorMessage.NOT_OWNER);
        }
    }
}
