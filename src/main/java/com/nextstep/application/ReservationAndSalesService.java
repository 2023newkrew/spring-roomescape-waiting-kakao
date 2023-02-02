package com.nextstep.application;

import com.authorizationserver.domains.authorization.enums.RoleType;
import com.authorizationserver.domains.authorization.exceptions.AuthenticationErrorMessageType;
import com.authorizationserver.domains.authorization.exceptions.AuthenticationException;
import com.authorizationserver.infrastructures.jwt.TokenData;
import com.nextstep.domains.exceptions.ErrorMessageType;
import com.nextstep.domains.exceptions.ReservationException;
import com.nextstep.domains.reservation.Reservation;
import com.nextstep.domains.reservation.ReservationService;
import com.nextstep.domains.reservation.enums.ReservationStatus;
import com.nextstep.domains.sales.SalesService;
import com.nextstep.interfaces.reservation.dtos.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationAndSalesService {
    private final ReservationService reservationService;

    private final ReservationMapper reservationMapper;

    private final SalesService salesService;

    public boolean approveById(TokenData tokenData, Long reservationId) {
        Reservation reservation = reservationMapper.fromResponse(reservationService.getById(reservationId));
        validateReservationAdmin(reservation, tokenData);
        if (!reservation.getStatus().equals(ReservationStatus.UNAPPROVED)){
            throw new ReservationException(ErrorMessageType.RESERVATION_STATUS_CONFLICT);
        }
        salesService.approveByReservationId(reservation);
        return reservationService.approveById(reservationId);
    }

    public boolean cancelByReservationId(TokenData tokenData, Long reservationId) {
        Reservation reservation = reservationMapper.fromResponse(reservationService.getById(reservationId));
        try {
            validateReservationAdmin(reservation, tokenData);
            if (reservation.getStatus().equals(ReservationStatus.UNAPPROVED)){
                return reservationService.cancelById(reservationId);
            }
            if (reservation.getStatus().equals(ReservationStatus.APPROVED)){
                System.out.println("11111");
                salesService.cancelByReservationId(reservationId);
                System.out.println("@22222222");
                return reservationService.cancelById(reservationId);
            }
            throw new ReservationException(ErrorMessageType.RESERVATION_STATUS_CONFLICT);
        } catch (AuthenticationException e){
            validateReservationMine(reservation, tokenData);
            if (reservation.getStatus().equals(ReservationStatus.UNAPPROVED)){
                return reservationService.cancelById(reservationId);
            }
            if (reservation.getStatus().equals(ReservationStatus.APPROVED)){
                salesService.cancelByReservationId(reservationId);
                return reservationService.cancelWaitById(reservationId);
            }
            throw new ReservationException(ErrorMessageType.RESERVATION_STATUS_CONFLICT);
        }
    }

    @Transactional
    public boolean cancelApproveByReservationId(TokenData tokenData, Long reservationId) {
        Reservation reservation = reservationMapper.fromResponse(reservationService.getById(reservationId));
        validateReservationAdmin(reservation, tokenData);
        if (!reservation.getStatus().equals(ReservationStatus.CANCELED_WAIT)){
            throw new ReservationException(ErrorMessageType.RESERVATION_STATUS_CONFLICT);
        }
        return reservationService.cancelApproveById(reservationId);
    }

    private void validateReservationMine(Reservation reservation, TokenData tokenData) {
        if (Objects.isNull(reservation)) {
            throw new ReservationException(ErrorMessageType.RESERVATION_NOT_EXISTS);
        }
        if (!tokenData.getId().equals(reservation.getMemberId())) {
            throw new ReservationException(ErrorMessageType.NOT_RESERVATION_OWNER);
        }
    }
    private void validateReservationAdmin(Reservation reservation, TokenData tokenData) {
        if (Objects.isNull(reservation)) {
            throw new ReservationException(ErrorMessageType.RESERVATION_NOT_EXISTS);
        }
        if (!tokenData.getRole().equals(RoleType.ADMIN.name())) {
            throw new AuthenticationException(AuthenticationErrorMessageType.NOT_ADMIN);
        }
    }
}
