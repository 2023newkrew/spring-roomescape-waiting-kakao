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
        Schedule schedule = scheduleService.getById(reservation.getScheduleId());
        validateSchedule(schedule);

        return repository.insert(reservation);
    }

    private void validateSchedule(Schedule schedule) {
        if (Objects.isNull(schedule)) {
            throw new ScheduleException(ScheduleErrorMessage.NOT_EXISTS);
        }
        if (existsBySchedule(schedule)) {
            throw new ReservationException(ReservationErrorMessage.CONFLICT);
        }
    }

    @Override
    public boolean existsBySchedule(Schedule schedule) {
        return repository.existsBySchedule(schedule);
    }

    @Override
    public Reservation getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public List<Reservation> getAllByMember(Member member) {
        return repository.getAllByMember(member);
    }

    @Transactional
    @Override
    public boolean deleteById(Member member, Long id) {
        Reservation reservation = repository.getById(id);
        validateReservation(member, reservation);
        boolean deleted = repository.deleteById(id);
        if (deleted) {
            publisher.publishEvent(reservation);
        }

        return deleted;
    }

    private void validateReservation(Member member, Reservation reservation) {
        if (Objects.isNull(reservation)) {
            throw new ReservationException(ReservationErrorMessage.NOT_EXISTS);
        }
        if (reservation.isNotOwner(member)) {
            throw new ReservationException(ReservationErrorMessage.NOT_OWNER);
        }
    }
}
