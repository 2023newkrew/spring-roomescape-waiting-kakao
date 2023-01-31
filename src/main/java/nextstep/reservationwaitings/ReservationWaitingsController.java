package nextstep.reservationwaitings;

import auth.LoginMember;
import auth.UserDetails;
import nextstep.support.UnauthorizedException;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Void> create(@LoginMember UserDetails member, @RequestBody ReservationWaitingRequest request) {
        ReservationResult result = reservationWaitingsService.create(member, request);
        if (result.isReservationWaiting()) {
            return ResponseEntity.created(URI.create("/reservation-waitings/" + result.getId())).build();
        }
        return ResponseEntity.created(URI.create("/reservations/" + result.getId())).build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaitings>> findMyReservationWaitings(@LoginMember UserDetails member) {
        List<ReservationWaitings> reservationWaitings = reservationWaitingsService.findMyReservationWaitings(member);
        return ResponseEntity.ok(reservationWaitings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@LoginMember UserDetails member, @PathVariable Long id) {
        reservationWaitingsService.delete(member, id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity onUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getStackTrace());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getStackTrace());
    }

}
