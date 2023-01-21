package nextstep.reservation.controller;

import lombok.RequiredArgsConstructor;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;
import nextstep.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/reservations")
@RestController
public class ReservationController {

    private static final String RESERVATION_PATH = "/reservations";

    private final ReservationService service;

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody @Validated ReservationRequest request) {
        ReservationResponse reservation = service.create(request);
        URI location = URI.create(RESERVATION_PATH + "/" + reservation.getId());

        return ResponseEntity.created(location)
                .build();
    }

    @GetMapping("/{reservation_id}")
    public ResponseEntity<?> getReservation(@PathVariable("reservation_id") Long reservationId) {

        return ResponseEntity.ok(service.getById(reservationId));
    }

    @DeleteMapping("/{reservation_id}")
    public ResponseEntity<?> deleteReservation(@PathVariable("reservation_id") Long reservationId) {

        return ResponseEntity.ok(service.deleteById(reservationId));
    }
}
