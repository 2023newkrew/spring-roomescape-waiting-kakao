package nextstep.reservation.controller;

import auth.resolver.MemberId;
import lombok.RequiredArgsConstructor;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;
import nextstep.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/reservations")
@RestController
public class ReservationController {

    private static final String RESERVATION_PATH = "/reservations/";

    private final ReservationService service;

    @PostMapping
    public ResponseEntity<Void> createReservation(
            @MemberId Long memberId,
            @RequestBody ReservationRequest request) {
        ReservationResponse reservation = service.create(memberId, request);
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

    @DeleteMapping("/{reservation_id}")
    public ResponseEntity<Boolean> deleteReservation(
            @MemberId Long memberId,
            @PathVariable("reservation_id") Long reservationId) {
        return ResponseEntity.ok(service.deleteById(memberId, reservationId));
    }
}
