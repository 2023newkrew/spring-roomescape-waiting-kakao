package nextstep.reservation;

import auth.LoginMember;
import auth.UserDetails;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberService;
import nextstep.reservation_waiting.ReservationWaitingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationWaitingService reservationWaitingService;
    private final ReservationService reservationService;
    private final MemberService memberService;

    @PostMapping("/reservations")
    public ResponseEntity<URI> createReservation(@LoginMember UserDetails userDetails, @RequestBody ReservationRequest reservationRequest) {
        Member member = memberService.findById(userDetails.getId());
        Long id = reservationService.create(member, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id))
                .build();
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok()
                .body(results);
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity<List<Reservation>> readOwnReservations(@LoginMember UserDetails userDetails) {
        Member member = memberService.findById(userDetails.getId());
        List<Reservation> own = reservationService.findOwn(member);
        return ResponseEntity.ok()
                .body(own);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        Member member = memberService.findById(userDetails.getId());
        Reservation reservation = reservationService.findById(id);
        reservationService.deleteById(reservation, member);
        reservationWaitingService.confirm(reservation);
        return ResponseEntity.noContent()
                .build();
    }

}
