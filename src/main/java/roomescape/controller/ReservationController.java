package roomescape.controller;

import auth.annotation.Admin;
import auth.annotation.JWTMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.ReservationControllerGetMineResponse;
import roomescape.controller.dto.ReservationControllerGetResponse;
import roomescape.controller.dto.ReservationsControllerPostBody;
import roomescape.service.ReservationService;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;

    @PostMapping(produces = "application/json;charset=utf-8")
    public ResponseEntity<Object> createReservation(@JWTMemberId Long memberId, @Valid @RequestBody ReservationsControllerPostBody body) {
        var id = service.createReservation(memberId, body);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .header("Location", String.format("/api/reservations/%d", id))
                             .build();
    }

    @Admin
    @PatchMapping(value = "/{id}/approve", produces = "application/json;charset=utf-8")
    public ResponseEntity<Void> approve(@PathVariable Long id) {
        service.approve(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PatchMapping(value = "/{id}/cancel", produces = "application/json;charset=utf-8")
    public ResponseEntity<Void> cancel(@JWTMemberId Long memberId, @PathVariable Long id) {
        service.cancel(memberId, id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Admin
    @PatchMapping(value = "/{id}/disapprove", produces = "application/json;charset=utf-8")
    public ResponseEntity<Void> disapprove(@PathVariable Long id) {
        service.disapprove(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Admin
    @PatchMapping(value = "/{id}/cancel-accept", produces = "application/json;charset=utf-8")
    public ResponseEntity<Void> cancelAccept(@PathVariable Long id) {
        service.cancelAccept(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping(value = "/{id}", produces = "application/json;charset=utf-8")
    public ResponseEntity<ReservationControllerGetResponse> findReservation(@PathVariable Long id) {
        var reservation = service.findReservation(id);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ReservationControllerGetResponse(
                                     reservation.getId(),
                                     reservation.getDate(),
                                     reservation.getTime(),
                                     reservation.getName(),
                                     reservation.getStatus(),
                                     reservation.getTheme().getId(),
                                     reservation.getTheme().getName(),
                                     reservation.getTheme().getDesc(),
                                     reservation.getTheme().getPrice()
                             ));
    }

    @GetMapping(value = "/mine", produces = "application/json;charset=utf-8")
    public ResponseEntity<ReservationControllerGetMineResponse> findMyReservation(@JWTMemberId Long memberId) {
        var reservations = service.findMyReservation(memberId);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(ReservationControllerGetMineResponse.from(reservations));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json;charset=utf-8")
    public ResponseEntity<Object> deleteReservation(@JWTMemberId Long memberId, @PathVariable Long id) {
        service.deleteReservation(memberId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }
}
