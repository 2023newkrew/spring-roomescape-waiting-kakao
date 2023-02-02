package nextstep.reservation;

import auth.LoginMember;
import auth.MemberDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity createReservation(@LoginMember MemberDetail member, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(member.toMember(), reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity<List<Reservation>> readAllReservation(@LoginMember MemberDetail member){
        List<Reservation> results = reservationService.findAllByMember(member.toMember());
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginMember MemberDetail member, @PathVariable Long id) {
        reservationService.deleteById(member.toMember(), id);

        return ResponseEntity.noContent().build();
    }
}
