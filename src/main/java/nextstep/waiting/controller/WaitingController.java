package nextstep.waiting.controller;

import auth.domain.LoginUser;
import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.exception.ReservationException;
import nextstep.reservation.mapper.ReservationMapper;
import nextstep.reservation.service.ReservationService;
import nextstep.waiting.domain.Waiting;
import nextstep.waiting.dto.WaitingResponse;
import nextstep.waiting.mapper.WaitingMapper;
import nextstep.waiting.service.WaitingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/reservation-waitings")
@RestController
public class WaitingController {

    private static final String WAITING_PATH = "/reservation-waitings/";

    private final WaitingService service;

    private final WaitingMapper mapper;

    private static final String RESERVATION_PATH = "/reservations/";

    private final ReservationService reservationService;

    private final ReservationMapper reservationMapper;

    @PostMapping
    public ResponseEntity<Void> create(
            @LoginUser Member member,
            @RequestBody ReservationRequest request) {
        Reservation reservation = reservationMapper.fromRequest(member, request);

        return ResponseEntity.created(createReservationOrWaiting(reservation)).build();
    }

    private URI createReservationOrWaiting(Reservation reservation) {
        try {
            reservation = reservationService.create(reservation);

            return URI.create(RESERVATION_PATH + reservation.getId());
        }
        catch (ReservationException e) {
            Waiting waiting = service.create(reservation);

            return URI.create(WAITING_PATH + waiting.getId());
        }
    }

    @GetMapping("/{waiting_id}")
    public ResponseEntity<WaitingResponse> getById(@PathVariable("waiting_id") Long waitingId) {
        Waiting waiting = service.getById(waitingId);

        return ResponseEntity.ok(mapper.toResponse(waiting));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<WaitingResponse>> getByMember(@LoginUser Member member) {
        return ResponseEntity.ok(getWaitings(member));
    }

    private List<WaitingResponse> getWaitings(Member member) {
        return service.getByMember(member)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{waiting_id}")
    public ResponseEntity<Boolean> deleteById(
            @LoginUser Member member,
            @PathVariable("waiting_id") Long waitingId) {
        return ResponseEntity.ok(service.deleteById(member, waitingId));
    }
}
