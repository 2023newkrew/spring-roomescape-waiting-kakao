package nextstep.reservationwaiting;

import auth.AuthenticationException;
import auth.LoginMember;
import auth.UserDetails;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import nextstep.member.Member;
import nextstep.member.MemberService;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationWaitingController {
    public final ReservationWaitingService reservationWaitingService;
    public final MemberService memberService;
    public final ReservationService reservationService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService, MemberService memberService, ReservationService reservationService) {
        this.reservationWaitingService = reservationWaitingService;
        this.memberService = memberService;
        this.reservationService = reservationService;
    }
    @PostMapping("/reservation-waitings")
    public ResponseEntity createReservationWaiting(@LoginMember UserDetails userDetails, @RequestBody ReservationWaitingRequest reservationWaitingRequest) {
        if (!reservationService.isScheduleReserved(reservationWaitingRequest.getScheduleId())) {
            Member member = memberService.findByUserDetatils(userDetails);
            Long id = reservationService.create(
                    member,
                    new ReservationRequest(reservationWaitingRequest.getScheduleId())
            );
            return ResponseEntity.created(URI.create("/reservations/" + id)).build();
        }
        Long id = reservationWaitingService.create(userDetails, reservationWaitingRequest);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @GetMapping("/reservation-waitings/mine")
    public ResponseEntity readReservationWaitings(@LoginMember UserDetails userDetails) {
        List<ReservationWaiting> results = reservationWaitingService.findAllByUserDetails(userDetails);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservation-waitings/{id}")
    public ResponseEntity deleteReservationWaiting(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        int deletedRowCount = reservationWaitingService.deleteById(userDetails, id);

        return ResponseEntity.ok().body(Collections.singletonMap("deletedReservationWaitingCount", deletedRowCount));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception e) {
        System.out.println(e.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
