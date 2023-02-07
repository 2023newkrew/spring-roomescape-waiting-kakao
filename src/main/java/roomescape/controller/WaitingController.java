package roomescape.controller;

import auth.annotation.JWTMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.ReservationsControllerPostBody;
import roomescape.controller.dto.WaitingControllerGetResponse;
import roomescape.service.WaitingService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/waiting")
@RequiredArgsConstructor
public class WaitingController {
    private final WaitingService service;


    @PostMapping(produces = "application/json;charset=utf-8")
    public ResponseEntity<Void> createReservation(@JWTMemberId Long memberId, @Valid @RequestBody ReservationsControllerPostBody body) {
        var location = service.createWaiting(
                memberId, body,
                (reservationId) -> String.format("/api/reservations/%d", reservationId),
                (waitingId) -> String.format("/api/waiting/%d", waitingId)
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                             .header("Location", location)
                             .build();
    }

    @GetMapping(value = "/mine", produces = "application/json;charset=utf-8")
    public ResponseEntity<WaitingControllerGetResponse> findReservation(@JWTMemberId Long memberId) {
        var waitings = service.findMyWaiting(memberId);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(WaitingControllerGetResponse.from(waitings));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json;charset=utf-8")
    public ResponseEntity<Void> deleteReservation(@JWTMemberId Long memberId, @PathVariable Long id) {
        service.deleteWaiting(memberId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }
}
