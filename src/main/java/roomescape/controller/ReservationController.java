package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.annotation.JWTBearerTokenSubject;
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
    public ResponseEntity<Object> createReservation(@JWTBearerTokenSubject String subject, @Valid @RequestBody ReservationsControllerPostBody body) {
        var id = service.createReservation(Long.parseLong(subject), body);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .header("Location", String.format("/api/reservations/%d", id))
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
                                     reservation.getTheme().getId(),
                                     reservation.getTheme().getName(),
                                     reservation.getTheme().getDesc(),
                                     reservation.getTheme().getPrice()
                             ));
    }

    @GetMapping(value = "/mine", produces = "application/json;charset=utf-8")
    public ResponseEntity<ReservationControllerGetMineResponse> findMyReservation(@JWTBearerTokenSubject String subject) {
        var reservations = service.findMyReservation(Long.parseLong(subject));
        return ResponseEntity.status(HttpStatus.OK)
                             .body(ReservationControllerGetMineResponse.from(reservations));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json;charset=utf-8")
    public ResponseEntity<Object> deleteReservation(@JWTBearerTokenSubject String subject, @PathVariable Long id) {
        service.deleteReservation(Long.parseLong(subject), id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }
}
