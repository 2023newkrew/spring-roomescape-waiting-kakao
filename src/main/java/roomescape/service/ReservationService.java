package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ReservationsControllerPostBody;
import roomescape.controller.errors.ErrorCode;
import roomescape.entity.Reservation;
import roomescape.repository.ReservationRepository;
import roomescape.repository.WaitingRepository;
import roomescape.service.exception.ServiceException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;

    public long createReservation(long callerId, ReservationsControllerPostBody body) {
        var id = reservationRepository.insert(body.getName(), body.getDate(), body.getTime(), body.getThemeId(), callerId);
        if (id.isEmpty()) {
            throw new ServiceException(ErrorCode.ALREADY_EXIST_RESERVATION_AT_TIME);
        }
        return id.get();
    }

    @Transactional(readOnly = true)
    public Reservation findReservation(Long id) {
        var reservation = reservationRepository.selectById(id);
        if (reservation.isEmpty()) {
            throw new ServiceException(ErrorCode.UNKNOWN_RESERVATION_ID);
        }
        return reservation.get();
    }

    @Transactional(readOnly = true)
    public List<Reservation> findMyReservation(Long memberId) {
        return reservationRepository.selectByMemberId(memberId);
    }

    public void deleteReservation(long callerId, Long id) {
        var reservation = reservationRepository.selectById(id);
        if (reservation.isEmpty()) {
            throw new ServiceException(ErrorCode.UNKNOWN_RESERVATION_ID);
        }
        if (reservation.get().getMemberId() != callerId) {
            throw new ServiceException(ErrorCode.UNAUTHORIZED_RESERVATION);
        }
        reservationRepository.delete(id);
        // 대기중인 차순위의 데이터를 가져온다.
        var firstWaiting = waitingRepository.selectFirstByGroup(
                reservation.get().getTheme().getId(),
                reservation.get().getDate(),
                reservation.get().getTime()
        );
        if (firstWaiting.isPresent()) {
            // 만약 차순위의 대기자가 있다면 바로 이를 추가한다.
            waitingRepository.delete(firstWaiting.get().getId());
            reservationRepository.insert(
                    firstWaiting.get().getName(),
                    firstWaiting.get().getDate(),
                    firstWaiting.get().getTime(),
                    firstWaiting.get().getTheme().getId(),
                    firstWaiting.get().getMemberId()
            );
        }
    }
}
