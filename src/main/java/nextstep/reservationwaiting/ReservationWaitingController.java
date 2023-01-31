package nextstep.reservationwaiting;

import auth.argumentResolver.LoginMember;
import lombok.RequiredArgsConstructor;
import nextstep.exception.ReservationException;
import nextstep.reservation.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservation-waitings")
@RequiredArgsConstructor
public class ReservationWaitingController {
    private final ReservationWaitingService reservationWaitingService;
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> createReservationWaiting(@LoginMember Long memberId, @RequestBody ReservationWaitingRequest reservationWaitingRequest) {
        Long id;
        try {
            id = reservationService.create(memberId, reservationWaitingRequest.toReservationRequest());
        } catch (ReservationException e) {
            id = reservationWaitingService.create(memberId, reservationWaitingRequest);
        }
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaitingResponse>> mine(@LoginMember Long memberId) {
        return ResponseEntity.ok(reservationWaitingService.findByMemberId(memberId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@LoginMember Long memberId, @PathVariable Long id) {
        reservationWaitingService.deleteById(memberId, id);
        return ResponseEntity.noContent().build();
    }
}
