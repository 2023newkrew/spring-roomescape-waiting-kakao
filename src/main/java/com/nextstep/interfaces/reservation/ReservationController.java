package com.nextstep.interfaces.reservation;

import com.nextstep.infrastructures.web.UseContext;
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

    private final ScheduleService scheduleService;

    private final WaitingService waitingService;

    @PostMapping
    public ResponseEntity<Void> createReservation(
            @UseContext Long memberId,
            @RequestBody ReservationRequest request) {
        ScheduleResponse scheduleResponse = scheduleService.getById(request.getScheduleId());
        validateSchedule(scheduleResponse);
        ReservationResponse reservation = service.create(memberId, request, scheduleResponse);
        URI location = URI.create(RESERVATION_PATH + reservation.getId());


        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{reservation_id}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable("reservation_id") Long reservationId) {
        return ResponseEntity.ok(service.getById(reservationId));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationResponse>> getByMemberId(@UseContext Long memberId) {
        return ResponseEntity.ok(service.getByMemberId(memberId));
    }

    @Transactional
    @DeleteMapping("/{reservation_id}")
    public ResponseEntity<Boolean> deleteReservation(
            @UseContext Long memberId,
            @PathVariable("reservation_id") Long reservationId) {
        Waiting waiting = waitingService.getFirstByScheduleId(reservationId);
        if (!Objects.isNull(waiting)){
            waitingService.deleteById(waiting.getMemberId(),waiting.getScheduleId());
        }
        return ResponseEntity.ok(service.deleteById(memberId, reservationId, waiting));
    }

    private void validateSchedule(ScheduleResponse schedule) {
        if (Objects.isNull(schedule)) {
            throw new ScheduleException(ErrorMessageType.SCHEDULE_NOT_EXISTS);
        }
    }

}
