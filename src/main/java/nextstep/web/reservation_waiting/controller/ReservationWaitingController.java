package nextstep.web.reservation_waiting.controller;

import auth.resolver.LoginMember;
import lombok.RequiredArgsConstructor;
import nextstep.web.member.domain.Member;
import nextstep.web.reservation_waiting.dto.ReservationWaitingRequest;
import nextstep.web.reservation_waiting.dto.ReservationWaitingResponse;
import nextstep.web.reservation_waiting.service.ReservationWaitingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {

    public final ReservationWaitingService reservationWaitingService;

    @PostMapping
    public ResponseEntity<Void> createReservationWaiting(
            @LoginMember Member member,
            @RequestBody ReservationWaitingRequest reservationWaitingRequest
    ) {
        String location = reservationWaitingService.create(member, reservationWaitingRequest);
        return ResponseEntity.created(URI.create(location)).build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaitingResponse>> readMyReservationWaiting(@LoginMember Member member) {
        List<ReservationWaitingResponse> results = reservationWaitingService.readMine(member);

        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationWaiting(@LoginMember Member member, @PathVariable Long id) {
        reservationWaitingService.deleteById(member, id);

        return ResponseEntity.noContent().build();
    }
}
