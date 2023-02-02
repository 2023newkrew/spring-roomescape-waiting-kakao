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
    public ReservationEntity create(ReservationEntity reservation) {
        validateSchedule(reservation.getScheduleId());

        return repository.insert(reservation);
    }

    private void validateSchedule(Long scheduleId) {
        ScheduleEntity scheduleEntity = scheduleService.getById(scheduleId);
        if (Objects.isNull(scheduleEntity)) {
            throw new ScheduleException(ScheduleErrorMessage.NOT_EXISTS);
        }
        if (repository.existsBySchedule(scheduleEntity)) {
            throw new ReservationException(ReservationErrorMessage.CONFLICT);
        }
    }

    @Override
    public ReservationEntity getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public List<ReservationEntity> getByMember(MemberEntity member) {
        return repository.getByMember(member);
    }

    @Transactional
    @Override
    public boolean deleteById(MemberEntity member, Long id) {
        ReservationEntity reservation = getValidReservation(member, id);
        boolean deleted = repository.deleteById(id);
        if (deleted) {
            publisher.publishEvent(reservation);
        }

        return deleted;
    }

    private ReservationEntity getValidReservation(MemberEntity member, Long id) {
        ReservationEntity reservation = repository.getById(id);
        validateReservation(member, reservation);

        return reservation;
    }

    private void validateReservation(MemberEntity memberEntity, ReservationEntity reservation) {
        if (Objects.isNull(reservation)) {
            throw new ReservationException(ReservationErrorMessage.NOT_EXISTS);
        }
        if (!Objects.equals(memberEntity.getId(), reservation.getMemberId())) {
            throw new ReservationException(ReservationErrorMessage.NOT_OWNER);
        }
    }
}
