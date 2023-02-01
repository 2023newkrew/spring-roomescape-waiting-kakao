package nextstep.waiting.service;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.MemberEntity;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.domain.ReservationEntity;
import nextstep.reservation.repository.ReservationRepository;
import nextstep.waiting.domain.WaitingEntity;
import nextstep.waiting.exception.WaitingErrorMessage;
import nextstep.waiting.exception.WaitingException;
import nextstep.waiting.repository.WaitingRepository;
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
    public WaitingEntity create(Reservation reservation) {
        ReservationEntity reservationEntity = reservation.toEntity();
        if (reservationRepository.existsByMemberAndSchedule(reservationEntity)) {
            throw new WaitingException(WaitingErrorMessage.ALREADY_RESERVED);
        }
        return tryInsert(toWaiting(reservationEntity));
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
}
