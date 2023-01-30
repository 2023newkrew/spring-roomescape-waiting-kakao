package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ReservationsControllerPostBody;
import roomescape.controller.errors.ErrorCode;
import roomescape.entity.Waiting;
import roomescape.repository.ReservationRepository;
import roomescape.repository.WaitingRepository;
import roomescape.service.exception.ServiceException;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public String createWaiting(long callerId, ReservationsControllerPostBody body, Function<Long, String> onReservationCreated, Function<Long, String> onWaitingCreated) {
        var reservationId = reservationRepository.insert(body.getName(), body.getDate(), body.getTime(), body.getThemeId(), callerId);
        if (reservationId.isPresent()) {
            return onReservationCreated.apply(reservationId.get());
        }
        return onWaitingCreated.apply(waitingRepository.insert(body.getName(), body.getDate(), body.getTime(), body.getThemeId(), callerId));
    }

    public List<Waiting> findMyWaiting(Long memberId) {
        return waitingRepository.selectByMemberId(memberId);
    }

    @Transactional
    public void deleteWaiting(long callerId, Long id) {
        var waiting = waitingRepository.selectById(id);
        if (waiting.isEmpty()) {
            throw new ServiceException(ErrorCode.UNKNOWN_WAITING_ID);
        }
        if (waiting.get().getMemberId() != callerId) {
            throw new ServiceException(ErrorCode.UNAUTHORIZED_WAITING);
        }
        waitingRepository.delete(id);
    }
}
