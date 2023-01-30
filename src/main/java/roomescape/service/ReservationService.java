package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ReservationsControllerPostBody;
import roomescape.controller.errors.ErrorCode;
import roomescape.entity.Reservation;
import roomescape.repository.ReservationRepository;
import roomescape.service.exception.ServiceException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository repository;

    @Transactional
    public long createReservation(long callerId, ReservationsControllerPostBody body) {
        var id = repository.insert(body.getName(), body.getDate(), body.getTime(), body.getThemeId(), callerId);
        if (id.isEmpty()) {
            throw new ServiceException(ErrorCode.ALREADY_EXIST_RESERVATION_AT_TIME);
        }
        return id.get();
    }


    public Reservation findReservation(Long id) {
        var reservation = repository.selectById(id);
        if (reservation.isEmpty()) {
            throw new ServiceException(ErrorCode.UNKNOWN_RESERVATION_ID);
        }
        return reservation.get();
    }

    public List<Reservation> findMyReservation(Long memberId) {
        return repository.selectByMemberId(memberId);
    }

    @Transactional
    public void deleteReservation(long callerId, Long id) {
        var reservation = repository.selectById(id);
        if (reservation.isEmpty()) {
            throw new ServiceException(ErrorCode.UNKNOWN_RESERVATION_ID);
        }
        if (reservation.get().getMemberId() != callerId) {
            throw new ServiceException(ErrorCode.UNAUTHORIZED_RESERVATION);
        }
        repository.delete(id);
    }
}
