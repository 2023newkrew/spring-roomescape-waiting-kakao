package nextstep.reservation;

import auth.AuthenticationException;
import auth.LoginMember;
import auth.LoginService;
import auth.TokenMember;
import nextstep.member.Member;
import nextstep.member.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {
    public final MemberService memberService;
    public final ReservationService reservationService;

    public ReservationController(MemberService memberService,ReservationService reservationService) {
        this.memberService = memberService;
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity createReservation(@LoginMember TokenMember member, @RequestBody ReservationRequest reservationRequest) {
        if(member == null){
            throw new AuthenticationException();
        }
        Long id = reservationService.create(memberService.findById(member.getId()), reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginMember TokenMember member, @PathVariable Long id) {
        if(member == null){
            throw new AuthenticationException();
        }
        reservationService.deleteById(memberService.findById(member.getId()), id);

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
