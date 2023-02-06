package com.nextstep.application;

import com.nextstep.domains.reservation.Reservation;
import com.nextstep.domains.reservation.ReservationService;
import com.nextstep.domains.reservation.enums.ReservationStatus;
import com.nextstep.domains.sales.SalesService;
import com.nextstep.interfaces.exceptions.ErrorMessageType;
import com.nextstep.interfaces.exceptions.ReservationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationAndSalesService {
    private final ReservationService reservationService;

    private final SalesService salesService;

    public boolean approve(Reservation reservation) {
        if (!reservation.getStatus().equals(ReservationStatus.UNAPPROVED)){
            throw new ReservationException(ErrorMessageType.RESERVATION_STATUS_CONFLICT);
        }
        salesService.approveByReservationId(reservation);
        return reservationService.approveById(reservation.getId());
    }

    public boolean cancelByReservationAdmin(Reservation reservation) {

        if (reservation.getStatus().equals(ReservationStatus.UNAPPROVED)){
            return reservationService.cancelById(reservation.getId());
        }
        if (reservation.getStatus().equals(ReservationStatus.APPROVED)){
            salesService.cancelByReservationId(reservation.getId());
            return reservationService.cancelById(reservation.getId());
        }
        throw new ReservationException(ErrorMessageType.RESERVATION_STATUS_CONFLICT);

    }
    public boolean cancelByReservationMember(Reservation reservation) {
        if (reservation.getStatus().equals(ReservationStatus.UNAPPROVED)){
                return reservationService.cancelById(reservation.getId());
        }
        if (reservation.getStatus().equals(ReservationStatus.APPROVED)){
            salesService.cancelByReservationId(reservation.getId());
            return reservationService.cancelWaitById(reservation.getId());
        }
        throw new ReservationException(ErrorMessageType.RESERVATION_STATUS_CONFLICT);

    }

    @Transactional
    public boolean cancelApproveByReservation(Reservation reservation) {
        if (!reservation.getStatus().equals(ReservationStatus.CANCELED_WAIT)){
            throw new ReservationException(ErrorMessageType.RESERVATION_STATUS_CONFLICT);
        }
        return reservationService.cancelApproveById(reservation.getId());
    }
}
