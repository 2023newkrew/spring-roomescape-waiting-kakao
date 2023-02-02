package roomwaiting.nextstep.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.NotAcceptableStatusException;
import roomwaiting.nextstep.reservation.dao.ReservationDao;
import roomwaiting.nextstep.reservation.domain.Reservation;
import roomwaiting.nextstep.sales.Sales;
import roomwaiting.nextstep.sales.SalesDao;

import static roomwaiting.nextstep.reservation.ReservationStatus.*;
import static roomwaiting.support.Messages.*;

@Service
public class ReservationAdminService {
    private final ReservationDao reservationDao;
    private final SalesDao salesDao;

    public ReservationAdminService(ReservationDao reservationDao, SalesDao salesDao) {
        this.reservationDao = reservationDao;
        this.salesDao = salesDao;
    }

    public void approve(Long id) {
        Reservation reservation = reservationDao.findById(id).orElseThrow(() ->
                new NullPointerException(RESERVATION_NOT_FOUND.getMessage())
        );
        if (reservation.getStatus() == NOT_APPROVED) {
            reservationDao.updateState(APPROVED, id);
            salesDao.updateSales(new Sales(reservation.getMember(),
                                    reservation.getSchedule().getTheme().getPrice(), reservation.getSchedule()));
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
            salesDao.updateSales(new Sales(reservation.getMember(),
                    -reservation.getSchedule().getTheme().getPrice(), reservation.getSchedule()));
            return;
        }
        throw new NotAcceptableStatusException(NEEDS_NOT_APPROVED_STATUS.getMessage());
    }
}
