package nextstep.domain.reservation;

import auth.exception.AuthorizationException;
import nextstep.domain.sales.SalesDao;
import nextstep.domain.schedule.Schedule;
import nextstep.error.ErrorCode;
import nextstep.error.exception.EntityNotFoundException;

import java.util.Objects;

public class ReservationManager {
    ReservationDao reservationDao;
    ReservationWaitingDao reservationWaitingDao;
    SalesDao salesDao;

    public ReservationManager(ReservationDao reservationDao, ReservationWaitingDao reservationWaitingDao, SalesDao salesDao) {
        this.reservationDao = reservationDao;
        this.reservationWaitingDao = reservationWaitingDao;
        this.salesDao = salesDao;
    }

    public Reservation acceptReservation(Long id) {
        Reservation reservation = reservationDao.findById(id);

        if (Objects.isNull(reservation)) throw new EntityNotFoundException(ErrorCode.RESERVATION_NOT_FOUND);
        if (!reservation.getState().equals(ReservationState.UNACCEPTED))
            throw new IllegalStateException("얘약 승인 대기 상태가 아닙니다");

        return reservationDao.acceptReservation(id);
    }

    public Reservation cancelReservation(Long memberId, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (!reservation.sameMember(memberId)) {
            throw new AuthorizationException();
        }

        if (reservation.getState().equals(ReservationState.ACCEPTED)) return cancelAcceptedReservation(id);

        return cancelUnacceptedReservation(id);
    }

    private synchronized Reservation cancelAcceptedReservation(Long id) {
        Reservation result = reservationDao.updateState(id, ReservationState.CANCEL_WAITING);

        salesDao.refund(id);

        updateWaiting(result.getSchedule());

        return result;
    }

    private synchronized Reservation cancelUnacceptedReservation(Long id) {
        Reservation result = reservationDao.updateState(id, ReservationState.CANCELED);

        updateWaiting(result.getSchedule());

        return result;
    }

    private void updateWaiting(Schedule schedule) {
        ReservationWaiting waiting = reservationWaitingDao.findByScheduleId(schedule.getId());

        if (!Objects.isNull(waiting)) {
            reservationDao.save(new Reservation(
                    waiting.getSchedule(),
                    waiting.getMember(),
                    ReservationState.UNACCEPTED
            ));
            reservationWaitingDao.deleteById(waiting.getId());
        }
    }

    public synchronized Reservation rejectReservation(Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation.getState().equals(ReservationState.REJECTED)) {
            throw new IllegalStateException("이미 거절된 예약입니다");
        }

        Reservation result = reservationDao.updateState(id, ReservationState.REJECTED);

        updateWaiting(result.getSchedule());
        
        return result;
    }

    public synchronized Reservation approveCancel(Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (!reservation.getState().equals(ReservationState.CANCEL_WAITING)) {
            throw new IllegalStateException("취소 대기 상태가 아닙니다");
        }

        Reservation result = reservationDao.updateState(id, ReservationState.CANCELED);

        salesDao.refund(id);

        return result;
    }
}
