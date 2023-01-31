package nextstep.reservation.controller;

import auth.domain.LoginUser;
import lombok.RequiredArgsConstructor;
import nextstep.member.domain.MemberEntity;
import nextstep.reservation.domain.ReservationEntity;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;
import nextstep.reservation.mapper.ReservationMapper;
import nextstep.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/reservations")
@RestController
public class ReservationController {

    private static final String RESERVATION_PATH = "/reservations/";

    private final ReservationService service;

    private final ReservationMapper mapper;

    @PostMapping
    public ResponseEntity<Void> createReservation(
            @LoginUser MemberEntity member,
            @RequestBody ReservationRequest request) {
        ReservationEntity reservation = service.create(mapper.fromRequest(member.getId(), request));
        URI location = URI.create(RESERVATION_PATH + reservation.getId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{reservation_id}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable("reservation_id") Long reservationId) {
        ReservationEntity reservation = service.getById(reservationId);
        return ResponseEntity.ok(mapper.toResponse(reservation));
    }

    @DeleteMapping("/{reservation_id}")
    public ResponseEntity<Boolean> deleteReservation(
            @LoginUser MemberEntity member,
            @PathVariable("reservation_id") Long reservationId) {
        return ResponseEntity.ok(service.deleteById(member.getId(), reservationId));
    }
}
