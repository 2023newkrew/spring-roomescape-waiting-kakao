package nextstep.waiting.controller;

import auth.resolver.MemberId;
import lombok.RequiredArgsConstructor;
import nextstep.etc.exception.ReservationException;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;
import nextstep.reservation.service.ReservationService;
import nextstep.waiting.dto.WaitingRequest;
import nextstep.waiting.dto.WaitingResponse;
import nextstep.waiting.service.WaitingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/reservation-waitings")
@RestController
public class WaitingController {

    private static final String WAITING_PATH = "/reservation-waitings/";

    private final WaitingService service;

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> create(
            @MemberId Long memberId,
            @RequestBody WaitingRequest request) {
        URI location;
        try {
            ReservationRequest reservationRequest = new ReservationRequest(request.getScheduleId());
            ReservationResponse reservation = reservationService.create(memberId, reservationRequest);
            location = URI.create("/reservations/" + reservation.getId());
        }
        catch (ReservationException e) {
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
}
