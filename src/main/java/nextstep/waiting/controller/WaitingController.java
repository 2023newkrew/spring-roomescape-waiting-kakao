package nextstep.waiting.controller;

import auth.resolver.MemberId;
import lombok.RequiredArgsConstructor;
import nextstep.etc.exception.ErrorMessage;
import nextstep.etc.exception.ReservationException;
import nextstep.etc.exception.ScheduleException;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;
import nextstep.reservation.service.ReservationService;
import nextstep.schedule.dto.ScheduleResponse;
import nextstep.schedule.service.ScheduleService;
import nextstep.waiting.dto.WaitingRequest;
import nextstep.waiting.dto.WaitingResponse;
import nextstep.waiting.service.WaitingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RequestMapping("/reservation-waitings")
@RestController
public class WaitingController {

    private static final String WAITING_PATH = "/reservation-waitings/";

    private final WaitingService service;

    private final ReservationService reservationService;

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Void> create(
            @MemberId Long memberId,
            @RequestBody WaitingRequest request) {
        URI location;
        ScheduleResponse scheduleResponse = scheduleService.getById(request.getScheduleId());
        validateSchedule(scheduleResponse);
        try {
            ReservationRequest reservationRequest = new ReservationRequest(request.getScheduleId());
            ReservationResponse reservation = reservationService.create(memberId, reservationRequest, scheduleResponse);
            location = URI.create("/reservations/" + reservation.getId());
        }
        catch (ReservationException e) {
            if (reservationService.existsByMemberIdAndScheduleId(memberId, request.getScheduleId())) {
                throw new ReservationException(ErrorMessage.RESERVATION_CONFLICT);
            }
            WaitingResponse waiting = service.create(memberId, request);
            location = URI.create(WAITING_PATH + waiting.getId());
        }
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{waiting_id}")
    public ResponseEntity<WaitingResponse> getById(@PathVariable("waiting_id") Long waitingId) {
        return ResponseEntity.ok(service.getById(waitingId));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<WaitingResponse>> getByMemberId(@MemberId Long memberId) {
        return ResponseEntity.ok(service.getByMemberId(memberId));
    }

    @DeleteMapping("/{waiting_id}")
    public ResponseEntity<Boolean> deleteById(
            @MemberId Long memberId,
            @PathVariable("waiting_id") Long waitingId) {
        return ResponseEntity.ok(service.deleteById(memberId, waitingId));
    }

    private void validateSchedule(ScheduleResponse schedule) {
        if (Objects.isNull(schedule)) {
            throw new ScheduleException(ErrorMessage.SCHEDULE_NOT_EXISTS);
        }
    }
}
