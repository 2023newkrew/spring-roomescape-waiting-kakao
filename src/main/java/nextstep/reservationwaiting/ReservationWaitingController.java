package nextstep.reservationwaiting;

import auth.AuthenticationException;
import auth.LoginMember;
import nextstep.member.MemberService;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import nextstep.support.DuplicateEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationWaitingController {
    private final ReservationWaitingService reservationWaitingService;
    private final ReservationService reservationService;
    private final MemberService memberService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService, ReservationService reservationService, MemberService memberService){
        this.reservationWaitingService = reservationWaitingService;
        this.reservationService = reservationService;
        this.memberService = memberService;
    }

    @PostMapping("/reservation-waitings")
    public ResponseEntity createReservationWaiting(@LoginMember Long memberId, @RequestBody ReservationWaitingRequest reservationWaitingRequest) {
        Long id;

        try {
            id = reservationService.create(memberService.findById(memberId), new ReservationRequest(reservationWaitingRequest.getScheduleId()));
            return ResponseEntity.created(URI.create("/reservations/" + id)).build();

        } catch (DuplicateEntityException e) {
            id = reservationWaitingService.create(memberId, reservationWaitingRequest);
            return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();

        }
    }

    @GetMapping("/reservation-waitings/mine")
    public ResponseEntity readReservationWaitings(@LoginMember Long memberId) {
        List<ReservationWaitingResponse> results = reservationWaitingService.findAllByMemberId(memberId);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservation-waitings/{id}")
    public ResponseEntity deleteReservationWaiting(@LoginMember Long memberId, @PathVariable Long id) {
        reservationWaitingService.deleteById(memberService.findById(memberId), id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> onException(Exception e) {
        System.err.println(e.getMessage());
        e.printStackTrace();
        
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
