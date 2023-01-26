package nextstep.waiting.controller;

import auth.resolver.MemberId;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public ResponseEntity<Void> create(
            @MemberId Long memberId,
            @RequestBody WaitingRequest request) {
        WaitingResponse reservation = service.create(memberId, request);
        URI location = URI.create(WAITING_PATH + reservation.getId());

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
