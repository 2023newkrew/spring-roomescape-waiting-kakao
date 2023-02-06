package nextstep.reservation.controller;

import auth.domain.LoginUser;
import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;
import nextstep.reservation.mapper.ReservationMapper;
import nextstep.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/reservations")
@RestController
public class ReservationController {

    private static final String RESERVATION_PATH = "/reservations/";

    private final ReservationService service;

    private final ReservationMapper mapper;

    @PostMapping
    public ResponseEntity<Void> createReservation(
            @LoginUser Member member,
            @RequestBody ReservationRequest request) {
        Reservation reservation = service.create(mapper.fromRequest(member, request));
        URI location = URI.create(RESERVATION_PATH + reservation.getId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{reservation_id}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable("reservation_id") Long reservationId) {
        Reservation reservation = service.getById(reservationId);

        return ResponseEntity.ok(mapper.toResponse(reservation));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationResponse>> getByMemberId(@LoginUser Member member) {
        return ResponseEntity.ok(getReservations(member));
    }

    private List<ReservationResponse> getReservations(Member member) {
        return service.getByMember(member)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{reservation_id}")
    public ResponseEntity<Boolean> deleteReservation(
            @LoginUser Member member,
            @PathVariable("reservation_id") Long reservationId) {
        return ResponseEntity.ok(service.deleteById(member, reservationId));
    }
}
