package nextstep.reservation;

import auth.LoginMember;
import auth.NeedAuth;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @NeedAuth
    @PostMapping("")
    public ResponseEntity createReservation(@LoginMember Long memberId, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(memberId, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @NeedAuth
    @GetMapping("/mine")
    public ResponseEntity findByMemberId(@LoginMember Long memberId) {
        List<Reservation> reservations = reservationService.findByMemberId(memberId);
        return ResponseEntity.ok().body(reservations);
    }

    @NeedAuth
    @DeleteMapping("/{id}")
    public ResponseEntity deleteReservation(@LoginMember Long memberId, @PathVariable Long id) {
        reservationService.deleteById(memberId, id);
        return ResponseEntity.noContent().build();
    }
}
