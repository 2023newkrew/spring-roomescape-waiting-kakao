package nextstep.reservationwaitings;

import auth.LoginMember;
import nextstep.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservation-waitings")
public class ReservationWaitingsController {

    private final ReservationWaitingsService reservationWaitingsService;

    public ReservationWaitingsController(ReservationWaitingsService reservationWaitingsService) {
        this.reservationWaitingsService = reservationWaitingsService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@LoginMember Member member, @RequestBody ReservationWaitingRequest request) {
        long id = reservationWaitingsService.create(member, request);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaitings>> findMyReservationWaitings(@LoginMember Member member) {
        List<ReservationWaitings> reservationWaitings = reservationWaitingsService.findMyReservationWaitings(member);
        return ResponseEntity.ok(reservationWaitings);
    }
}
