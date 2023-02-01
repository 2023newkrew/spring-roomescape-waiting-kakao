package roomescape.service;

import errors.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ReservationsControllerPostBody;
import roomescape.entity.Reservation;
import roomescape.exception.ServiceException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.SalesRepository;
import roomescape.repository.WaitingRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;
    private final SalesRepository salesRepository;

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

    public void approve(Long id) {
        var reservation = reservationRepository.selectById(id);
        if (reservation.isEmpty()) {
            throw new ServiceException(ErrorCode.UNKNOWN_RESERVATION_ID);
        }
        switch (reservation.get().getStatus()) {
            case Approved -> {
            }
            case Unapproved -> {
                reservationRepository.updateStatus(id, Reservation.Status.Approved);
                salesRepository.insert(
                        String.format("%d 예약이 승인됨", id),
                        BigDecimal.valueOf(reservation.get().getTheme().getPrice()),
                        Optional.of(id)
                );
            }
            default -> throw new ServiceException(ErrorCode.IMPOSSIBLE_CANCEL);
        }
    }

    public void cancel(Long memberId, Long id) {
        var reservation = reservationRepository.selectById(id);
        if (reservation.isEmpty()) {
            throw new ServiceException(ErrorCode.UNKNOWN_RESERVATION_ID);
        }
        if (!reservation.get().getMemberId().equals(memberId)) {
            throw new ServiceException(ErrorCode.UNAUTHORIZED_RESERVATION);
        }
        switch (reservation.get().getStatus()) {
            case Approved -> reservationRepository.updateStatus(id, Reservation.Status.CancelRequested);
            case Unapproved -> reservationRepository.updateStatus(id, Reservation.Status.Canceled);
            case CancelRequested, Canceled -> {
            }
            default -> throw new ServiceException(ErrorCode.IMPOSSIBLE_CANCEL);
        }
    }

    public void disapprove(Long id) {
        var reservation = reservationRepository.selectById(id);
        if (reservation.isEmpty()) {
            throw new ServiceException(ErrorCode.UNKNOWN_RESERVATION_ID);
        }
        switch (reservation.get().getStatus()) {
            case Approved -> {
                reservationRepository.updateStatus(id, Reservation.Status.Disapprove);
                salesRepository.insert(
                        String.format("%d 예약이 승인된 후 거부됨", id),
                        BigDecimal.valueOf(reservation.get().getTheme().getPrice()).negate(),
                        Optional.of(id)
                );
            }
            case Unapproved -> reservationRepository.updateStatus(id, Reservation.Status.Disapprove);
            default -> throw new ServiceException(ErrorCode.IMPOSSIBLE_DISAPPROVE);
        }
    }

    public void cancelAccept(Long id) {
        var reservation = reservationRepository.selectById(id);
        if (reservation.isEmpty()) {
            throw new ServiceException(ErrorCode.UNKNOWN_RESERVATION_ID);
        }
        switch (reservation.get().getStatus()) {
            case CancelRequested -> {
                reservationRepository.updateStatus(id, Reservation.Status.Canceled);
                salesRepository.insert(
                        String.format("%d 예약이 승인된 후 취소요구됨, 관리자 권한으로 취소를 허가함.", id),
                        BigDecimal.valueOf(reservation.get().getTheme().getPrice()).negate(),
                        Optional.of(id)
                );
            }
            default -> throw new ServiceException(ErrorCode.IMPOSSIBLE_CANCEL_ACCEPT);
        }
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
