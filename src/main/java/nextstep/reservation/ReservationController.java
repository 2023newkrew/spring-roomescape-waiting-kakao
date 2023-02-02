package nextstep.reservation;

import auth.AuthenticationException;
import auth.LoginMember;
import nextstep.member.Member;
import nextstep.support.DataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Void> reserveReservation(@LoginMember Member member, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.reserve(member, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<ReservationResponse>>> findReservations(@RequestParam Long themeId, @RequestParam Date date) {
        List<ReservationResponse> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(DataResponse.of(results));
    }

    @GetMapping("/mine")
    public ResponseEntity<DataResponse<List<ReservationResponse>>> findMyReservation(@LoginMember Member member) {
        List<ReservationResponse> results = reservationService.findMemberReservations(member);
        return ResponseEntity.ok().body(DataResponse.of(results));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@LoginMember Member member, @PathVariable Long id) {
        reservationService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> onException(Exception e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
