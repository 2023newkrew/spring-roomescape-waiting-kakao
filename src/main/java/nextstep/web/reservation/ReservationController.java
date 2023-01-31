package nextstep.web.reservation;

import auth.login.LoginMember;
import auth.login.MemberDetail;
import nextstep.web.member.Member;
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
    public ResponseEntity<Void> createReservation(@LoginMember MemberDetail member, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(Member.fromMemberDetail(member), reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity<List<Reservation>> readAllReservation(@LoginMember MemberDetail member){
        List<Reservation> results = reservationService.findAllByMember(Member.fromMemberDetail(member));
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@LoginMember MemberDetail member, @PathVariable Long id) {
        reservationService.deleteById(Member.fromMemberDetail(member), id);

        return ResponseEntity.noContent().build();
    }
}
