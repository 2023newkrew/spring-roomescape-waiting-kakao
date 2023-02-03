package nextstep.reservation;

import auth.LoginMember;
import auth.login.UserDetails;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberService;
import nextstep.reservation_waiting.ReservationWaitingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationWaitingService reservationWaitingService;
    private final ReservationService reservationService;
    private final MemberService memberService;

    @PostMapping("/reservations")
    public ResponseEntity createReservation(@LoginMember UserDetails userDetails, @RequestBody ReservationRequest reservationRequest) {
        Member member = memberService.findById(userDetails.getId());
        Long id = reservationService.create(member, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id))
            .build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
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
    public ResponseEntity deleteReservation(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        Member member = memberService.findById(userDetails.getId());
        Reservation reservation = reservationService.findById(id);
        reservationService.deleteById(reservation, member);
        reservationWaitingService.confirm(reservation);
        return ResponseEntity.noContent()
            .build();
    }

}
