package nextstep.reservation.controller;

import auth.resolver.MemberId;
import lombok.RequiredArgsConstructor;
import nextstep.etc.exception.ErrorMessage;
import nextstep.etc.exception.ScheduleException;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;
import nextstep.reservation.service.ReservationService;
import nextstep.schedule.dto.ScheduleResponse;
import nextstep.schedule.service.ScheduleService;
import nextstep.waiting.domain.Waiting;
import nextstep.waiting.service.WaitingService;
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
            @MemberId Long memberId,
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
    public ResponseEntity<List<ReservationResponse>> getByMemberId(@MemberId Long memberId) {
        return ResponseEntity.ok(service.getByMemberId(memberId));
    }

    @Transactional
    @DeleteMapping("/{reservation_id}")
    public ResponseEntity<Boolean> deleteReservation(
            @MemberId Long memberId,
            @PathVariable("reservation_id") Long reservationId) {
        Waiting waiting = waitingService.getFirstByScheduleId(reservationId);
        if (!Objects.isNull(waiting)){
            waitingService.deleteById(waiting.getMemberId(),waiting.getScheduleId());
        }
        return ResponseEntity.ok(service.deleteById(memberId, reservationId, waiting));
    }

    private void validateSchedule(ScheduleResponse schedule) {
        if (Objects.isNull(schedule)) {
            throw new ScheduleException(ErrorMessage.SCHEDULE_NOT_EXISTS);
        }
    }

}
