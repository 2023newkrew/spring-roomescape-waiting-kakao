package nextstep.waiting.service;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.repository.ReservationRepository;
import nextstep.schedule.domain.Schedule;
import nextstep.waiting.domain.Waiting;
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
    public Waiting create(Reservation reservation) {
        if (reservationRepository.existsByMemberAndSchedule(reservation)) {
            throw new WaitingException(WaitingErrorMessage.ALREADY_RESERVED);
        }
        return tryInsert(toWaiting(reservation));
    }

    private Waiting tryInsert(Waiting waiting) {
        try {
            return repository.insert(waiting);
        }
        catch (DataIntegrityViolationException ignore) {
            throw new WaitingException(WaitingErrorMessage.CONFLICT);
        }
    }

    private static Waiting toWaiting(Reservation reservation) {
        return new Waiting(null, reservation.getMember(), reservation.getSchedule(), null);
    }


    @Override
    public Waiting getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public List<Waiting> getByMember(Member member) {
        return repository.getByMember(member);
    }

    @Transactional
    @Override
    public boolean deleteById(Member member, Long id) {
        Waiting waiting = repository.getById(id);
        validateWaiting(waiting, member);

        return repository.deleteById(id);
    }

    private void validateWaiting(Waiting waiting, Member member) {
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
    public void onReservationDeleted(Reservation reservation) {
        Schedule schedule = reservation.getSchedule();
        Waiting waiting = repository.getFirstBySchedule(schedule);
        if (Objects.nonNull(waiting)) {
            Reservation newReservation = new Reservation(null, waiting.getMember(), schedule);
            repository.deleteById(waiting.getId());
            reservationRepository.insert(newReservation);
        }
    }
}
