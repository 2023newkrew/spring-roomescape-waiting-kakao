package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.annotation.JWTBearerTokenSubject;
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
    public ResponseEntity<Void> createReservation(@JWTBearerTokenSubject String subject, @Valid @RequestBody ReservationsControllerPostBody body) {
        var location = service.createWaiting(
                Long.parseLong(subject), body,
                (reservationId) -> String.format("/api/reservations/%d", reservationId),
                (waitingId) -> String.format("/api/waiting/%d", waitingId)
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                             .header("Location", location)
                             .build();
    }

    @GetMapping(value = "/mine", produces = "application/json;charset=utf-8")
    public ResponseEntity<WaitingControllerGetResponse> findReservation(@JWTBearerTokenSubject String subject) {
        var waitings = service.findMyWaiting(Long.parseLong(subject));
        return ResponseEntity.status(HttpStatus.OK)
                             .body(WaitingControllerGetResponse.from(waitings));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json;charset=utf-8")
    public ResponseEntity<Void> deleteReservation(@JWTBearerTokenSubject String subject, @PathVariable Long id) {
        service.deleteWaiting(Long.parseLong(subject), id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }
}
