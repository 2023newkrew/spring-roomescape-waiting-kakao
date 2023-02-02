package nextstep.reservation_waiting;

import auth.LoginMember;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.reservation.ReservationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {
    private final ReservationWaitingService reservationWaitingService;

    @PostMapping
    public ResponseEntity<Void> create(@LoginMember Member member, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationWaitingService.create(member, reservationRequest);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@LoginMember Member member, @PathVariable Long id) {
        reservationWaitingService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaitingResponse>> showMyReservationWaiting(
            @LoginMember Member member
    ) {
        List<ReservationWaitingResponse> reservationWaiting = reservationWaitingService.findAllByMember(member);
        return ResponseEntity.ok(reservationWaiting);
    }
}
