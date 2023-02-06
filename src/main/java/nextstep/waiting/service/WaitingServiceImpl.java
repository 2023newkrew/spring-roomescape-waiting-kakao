package nextstep.waiting.service;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.repository.ReservationRepository;
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
        boolean reservationExists = reservationRepository.existsByMemberAndSchedule(
                reservation.getMember(),
                reservation.getSchedule()
        );
        if (reservationExists) {
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
    public List<Waiting> getAllByMember(Member member) {
        return repository.getAllByMember(member);
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
        if (waiting.isNotOwner(member)) {
            throw new WaitingException(WaitingErrorMessage.NOT_OWNER);
        }
    }

    @Transactional
    @EventListener
    public void onReservationDeleted(Reservation reservation) {
        Waiting waiting = repository.getFirstBySchedule(reservation.getSchedule());
        if (Objects.nonNull(waiting)) {
            changeToReservation(waiting);
        }
    }

    private void changeToReservation(Waiting waiting) {
        Reservation newReservation = new Reservation(null, waiting.getMember(), waiting.getSchedule());
        repository.deleteById(waiting.getId());
        reservationRepository.insert(newReservation);
    }
}
