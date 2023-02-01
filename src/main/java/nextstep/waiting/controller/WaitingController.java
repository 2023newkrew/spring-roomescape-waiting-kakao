package nextstep.waiting.controller;

import auth.domain.LoginUser;
import lombok.RequiredArgsConstructor;
import nextstep.member.domain.MemberEntity;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.domain.ReservationEntity;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.exception.ReservationException;
import nextstep.reservation.mapper.ReservationMapper;
import nextstep.reservation.service.ReservationService;
import nextstep.waiting.domain.WaitingEntity;
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
            @LoginUser MemberEntity member,
            @RequestBody ReservationRequest request) {
        Reservation reservation = reservationMapper.fromRequest(member, request);

        return ResponseEntity.created(createReservationOrWaiting(reservation)).build();
    }

    private URI createReservationOrWaiting(Reservation reservation) {
        try {
            ReservationEntity reservationEntity = reservationService.create(reservation);

            return URI.create(RESERVATION_PATH + reservationEntity.getId());
        }
        catch (ReservationException e) {
            WaitingEntity waitingEntity = service.create(reservation);

            return URI.create(WAITING_PATH + waitingEntity.getId());
        }
    }

    @GetMapping("/{waiting_id}")
    public ResponseEntity<WaitingResponse> getById(@PathVariable("waiting_id") Long waitingId) {
        WaitingEntity waiting = service.getById(waitingId);

        return ResponseEntity.ok(mapper.toResponse(waiting));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<WaitingResponse>> getByMember(@LoginUser MemberEntity member) {
        return ResponseEntity.ok(getWaitings(member));
    }

    private List<WaitingResponse> getWaitings(MemberEntity member) {
        return service.getByMember(member)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{waiting_id}")
    public ResponseEntity<Boolean> deleteById(
            @LoginUser MemberEntity member,
            @PathVariable("waiting_id") Long waitingId) {
        return ResponseEntity.ok(service.deleteById(member, waitingId));
    }
}
