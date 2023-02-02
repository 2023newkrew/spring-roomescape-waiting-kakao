package roomwaiting.nextstep.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.NotAcceptableStatusException;
import roomwaiting.nextstep.reservation.dao.ReservationDao;
import roomwaiting.nextstep.reservation.domain.Reservation;

import static roomwaiting.nextstep.reservation.ReservationStatus.*;
import static roomwaiting.support.Messages.*;

@Service
public class ReservationAdminService {
    private final ReservationDao reservationDao;

    public ReservationAdminService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public void approve(Long id) {
        Reservation reservation = reservationDao.findById(id).orElseThrow(() ->
                new NullPointerException(RESERVATION_NOT_FOUND.getMessage())
        );
        if (reservation.getStatus() == NOT_APPROVED) {
            reservationDao.updateState(APPROVED, id);
            return;
        }
        throw new NotAcceptableStatusException(NEEDS_NOT_APPROVED_STATUS.getMessage());
    }

    public void cancelApprove(Long id){
        Reservation reservation = reservationDao.findById(id).orElseThrow(() ->
                new NullPointerException(RESERVATION_NOT_FOUND.getMessage())
        );
        if (reservation.getStatus() == CANCEL_WAIT){
            reservationDao.updateState(CANCEL, id);
            return;
        }
        throw new NotAcceptableStatusException(NEEDS_NOT_APPROVED_STATUS.getMessage());
    }
}
