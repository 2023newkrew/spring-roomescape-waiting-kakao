package nextstep.reservationwaiting;

import auth.AuthenticationException;
import auth.LoginMember;
import nextstep.member.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationWaitingController {
    private final ReservationWaitingService reservationWaitingService;
    private final MemberService memberService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService, MemberService memberService){
        this.reservationWaitingService = reservationWaitingService;
        this.memberService = memberService;
    }

    @PostMapping("/reservation-waitings")
    public ResponseEntity createReservationWaiting(@LoginMember Long memberId, @RequestBody ReservationWaitingRequest reservationWaitingRequest) {
        Long id = reservationWaitingService.create(memberId, reservationWaitingRequest);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

//    @GetMapping("/reservation-waitings/mine")
//    public ResponseEntity readReservationWaitings(@LoginMember Long memberId) {
//        List<ReservationWaiting> results = reservationWaitingService.findAllByMemberId(memberId);
//        return ResponseEntity.ok().body(results);
//    }
//
//    @DeleteMapping("/reservation-waitings/{id}")
//    public ResponseEntity deleteReservationWaiting(@LoginMember Long memberId, @PathVariable Long id) {
//        reservationWaitingService.deleteById(memberService.findById(memberId), id);
//        return ResponseEntity.noContent().build();
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
