package nextstep.reservationwaitings;

import auth.LoginMember;
import nextstep.member.Member;
import nextstep.support.NotCreatorMemberException;
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
        ReservationResult result = reservationWaitingsService.create(member, request);
        if (result.isReservationWaiting()) {
            return ResponseEntity.created(URI.create("/reservation-waitings/" + result.getId())).build();
        }
        return ResponseEntity.created(URI.create("/reservations/" + result.getId())).build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaitings>> findMyReservationWaitings(@LoginMember Member member) {
        List<ReservationWaitings> reservationWaitings = reservationWaitingsService.findMyReservationWaitings(member);
        return ResponseEntity.ok(reservationWaitings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@LoginMember Member member, @PathVariable Long id) {
        reservationWaitingsService.delete(member, id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NotCreatorMemberException.class)
    public ResponseEntity onException(NotCreatorMemberException e) {
        return ResponseEntity.badRequest().build();
    }
}
