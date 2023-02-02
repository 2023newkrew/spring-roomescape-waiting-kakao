package nextstep.reservation;

import auth.utils.AuthenticationException;
import auth.dto.LoginMember;
import auth.dto.UserDetails;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    public final ReservationService reservationService;
    public final MemberService memberService;

    @PostMapping("/reservations")
    public ResponseEntity createReservation(@LoginMember UserDetails userDetails, @RequestBody ReservationRequest reservationRequest) {
        Member member = memberService.findById(userDetails.getId());
        Long id = reservationService.create(member, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity readReservations(@LoginMember UserDetails userDetails) {
        List<ReservationResponse> results = reservationService.findAllByMemberId(userDetails.getId());
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        Member member = memberService.findById(userDetails.getId());
        reservationService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
