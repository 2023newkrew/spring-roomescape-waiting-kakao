package nextstep.reservation;

import auth.AuthenticationException;
import auth.LoginMember;
import auth.MemberDetails;
import nextstep.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity createReservation(@LoginMember MemberDetails memberDetails, @RequestBody ReservationRequest reservationRequest) {
        checkValid(memberDetails);
        Long id = reservationService.create((Member) memberDetails, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginMember MemberDetails memberDetails, @PathVariable Long id) {
        checkValid(memberDetails);
        reservationService.deleteById((Member) memberDetails, id);

        return ResponseEntity.noContent().build();
    }

    private static void checkValid(MemberDetails memberDetails) {
        if (Objects.isNull(memberDetails)) {
            throw new AuthenticationException();
        }
    }
}
