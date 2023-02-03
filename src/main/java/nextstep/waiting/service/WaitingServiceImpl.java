package nextstep.waiting.service;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.MemberEntity;
import nextstep.reservation.domain.ReservationEntity;
import nextstep.reservation.repository.ReservationRepository;
import nextstep.schedule.domain.ScheduleEntity;
import nextstep.waiting.domain.WaitingEntity;
import nextstep.waiting.exception.WaitingErrorMessage;
import nextstep.waiting.exception.WaitingException;
import nextstep.waiting.repository.WaitingRepository;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class WaitingServiceImpl implements WaitingService {

    private final WaitingRepository repository;

    private final ReservationRepository reservationRepository;

    @Transactional
    @Override
    public WaitingEntity create(ReservationEntity reservation) {
        if (reservationRepository.existsByMemberAndSchedule(reservation)) {
            throw new WaitingException(WaitingErrorMessage.ALREADY_RESERVED);
        }
        return tryInsert(toWaiting(reservation));
    }

    private WaitingEntity tryInsert(WaitingEntity waiting) {
        try {
            return repository.insert(waiting);
        }
        catch (DataIntegrityViolationException ignore) {
            throw new WaitingException(WaitingErrorMessage.CONFLICT);
        }
    }

    private static WaitingEntity toWaiting(ReservationEntity reservation) {
        return new WaitingEntity(null, reservation.getMember(), reservation.getSchedule(), null);
    }


    @Override
    public WaitingEntity getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public List<WaitingEntity> getByMember(MemberEntity member) {
        return repository.getByMember(member);
    }

    @Transactional
    @Override
    public boolean deleteById(MemberEntity member, Long id) {
        WaitingEntity waiting = repository.getById(id);
        validateWaiting(waiting, member);

        return repository.deleteById(id);
    }

    private void validateWaiting(WaitingEntity waiting, MemberEntity member) {
        if (Objects.isNull(waiting)) {
            throw new WaitingException(WaitingErrorMessage.NOT_EXISTS);
        }
        if (!Objects.equals(waiting.getMemberId(), member.getId())) {
            throw new WaitingException(WaitingErrorMessage.NOT_OWNER);
        }
    }

    @Transactional
    @EventListener
    @Override
    public void onReservationDeleted(ReservationEntity reservation) {
        ScheduleEntity schedule = reservation.getSchedule();
        WaitingEntity waiting = repository.getFirstBySchedule(schedule);
        if (Objects.nonNull(waiting)) {
            ReservationEntity newReservation = new ReservationEntity(null, waiting.getMember(), schedule);
            repository.deleteById(waiting.getId());
            reservationRepository.insert(newReservation);
        }
    }
}
