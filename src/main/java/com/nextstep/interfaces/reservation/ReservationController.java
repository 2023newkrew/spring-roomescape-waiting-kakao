package com.nextstep.interfaces.reservation;

import com.authorizationserver.domains.authorization.enums.RoleType;
import com.authorizationserver.domains.authorization.exceptions.AuthenticationErrorMessageType;
import com.authorizationserver.domains.authorization.exceptions.AuthenticationException;
import com.authorizationserver.infrastructures.jwt.TokenData;
import com.nextstep.application.ReservationAndSalesService;
import com.nextstep.domains.reservation.Reservation;
import com.nextstep.infrastructures.web.UseContext;
import com.nextstep.interfaces.exceptions.ReservationException;
import com.nextstep.interfaces.reservation.dtos.ReservationMapper;
import com.nextstep.interfaces.reservation.dtos.ReservationRequest;
import com.nextstep.interfaces.reservation.dtos.ReservationResponse;
import com.nextstep.domains.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import com.nextstep.interfaces.exceptions.ErrorMessageType;
import com.nextstep.interfaces.exceptions.ScheduleException;
import com.nextstep.interfaces.schedule.dtos.ScheduleResponse;
import com.nextstep.domains.schedule.ScheduleService;
import com.nextstep.domains.waiting.Waiting;
import com.nextstep.domains.waiting.WaitingService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RequestMapping("/reservations")
@RestController
public class ReservationController {

    private static final String RESERVATION_PATH = "/reservations/";

    private final ReservationService service;

    private final ReservationAndSalesService reservationAndSalesService;

    private final ScheduleService scheduleService;

    private final WaitingService waitingService;

    private final ReservationMapper reservationMapper;

    @PostMapping
    public ResponseEntity<Void> createReservation(
            @UseContext TokenData tokenData,
            @RequestBody ReservationRequest request) {
        ScheduleResponse scheduleResponse = scheduleService.getById(request.getScheduleId());
        validateSchedule(scheduleResponse);
        ReservationResponse reservation = service.create(tokenData.getId(), request, scheduleResponse);
        URI location = URI.create(RESERVATION_PATH + reservation.getId());


        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{reservation_id}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable("reservation_id") Long reservationId) {
        return ResponseEntity.ok(service.getById(reservationId));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationResponse>> getByMemberId(@UseContext TokenData tokenData) {
        return ResponseEntity.ok(service.getByMemberId(tokenData.getId()));
    }

    @Transactional
    @DeleteMapping("/{reservation_id}")
    public ResponseEntity<Boolean> deleteReservation(
            @UseContext TokenData tokenData,
            @PathVariable("reservation_id") Long reservationId) {
        Reservation reservation = reservationMapper.fromResponse(service.getById(reservationId));
        Waiting waiting = waitingService.getFirstByScheduleId(reservationId);
        if (!Objects.isNull(waiting)){
            waitingService.deleteById(waiting.getMemberId(),waiting.getScheduleId());
        }
        validateReservation(reservation);
        validateReservationMine(reservation, tokenData);
        return ResponseEntity.ok(service.deleteById(reservationId, waiting));
    }

    @PatchMapping("/{reservation_id}/approve")
    public ResponseEntity<Boolean> approveReservation(
            @UseContext TokenData tokenData,
            @PathVariable("reservation_id") Long reservationId
    ){
        Reservation reservation = reservationMapper.fromResponse(service.getById(reservationId));
        validateReservation(reservation);
        validateReservationAdmin(tokenData);
        return ResponseEntity.ok(reservationAndSalesService.approve(reservation));
    }

    @PatchMapping("/{reservation_id}/cancel")
    public ResponseEntity<Boolean> cancelReservation(
            @UseContext TokenData tokenData,
            @PathVariable("reservation_id") Long reservationId
    ){
        Reservation reservation = reservationMapper.fromResponse(service.getById(reservationId));
        validateReservation(reservation);
        try {
            validateReservationAdmin(tokenData);
            return ResponseEntity.ok(reservationAndSalesService.cancelByReservationAdmin(reservation));
        } catch (AuthenticationException e){
            validateReservationMine(reservation, tokenData);
            return ResponseEntity.ok(reservationAndSalesService.cancelByReservationMember(reservation));
        }
    }

    @PatchMapping("/{reservation_id}/cancel-approve")
    public ResponseEntity<Boolean> cancelApproveReservation(
            @UseContext TokenData tokenData,
            @PathVariable("reservation_id") Long reservationId
    ){
        Reservation reservation = reservationMapper.fromResponse(service.getById(reservationId));
        validateReservation(reservation);
        validateReservationAdmin(tokenData);
        return ResponseEntity.ok(reservationAndSalesService.cancelApproveByReservation(reservation));
    }

    private void validateSchedule(ScheduleResponse schedule) {
        if (Objects.isNull(schedule)) {
            throw new ScheduleException(ErrorMessageType.SCHEDULE_NOT_EXISTS);
        }
    }

    private void validateReservationMine(Reservation reservation, TokenData tokenData) {
        if (!tokenData.getId().equals(reservation.getMemberId())) {
            throw new ReservationException(ErrorMessageType.NOT_RESERVATION_OWNER);
        }
    }
    private void validateReservationAdmin(TokenData tokenData) {
        if (!tokenData.getRole().equals(RoleType.ADMIN.name())) {
            throw new AuthenticationException(AuthenticationErrorMessageType.NOT_ADMIN);
        }
    }

    private void validateReservation(Reservation reservation){
        if (Objects.isNull(reservation)) {
            throw new ReservationException(ErrorMessageType.RESERVATION_NOT_EXISTS);
        }
    }

}
