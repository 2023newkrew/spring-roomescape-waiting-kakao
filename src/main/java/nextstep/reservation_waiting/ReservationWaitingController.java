package nextstep.reservation_waiting;

import auth.LoginMember;
import auth.MemberDetail;
import lombok.RequiredArgsConstructor;
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
            @LoginMember MemberDetail member,
            @RequestBody ReservationWaitingRequest reservationWaitingRequest
    ) {
        URI locationUri = reservationWaitingService.create(member.toMember(), reservationWaitingRequest);
        return ResponseEntity.created(locationUri).build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaitingResponse>> readMyReservationWaiting(@LoginMember MemberDetail member) {
        List<ReservationWaitingResponse> results = reservationWaitingService.readMine(member.toMember());
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationWaiting(
            @LoginMember MemberDetail member,
            @PathVariable Long id
    ) {
        reservationWaitingService.deleteById(member.toMember(), id);
        return ResponseEntity.noContent().build();
    }
}
