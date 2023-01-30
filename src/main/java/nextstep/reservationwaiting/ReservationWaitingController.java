package nextstep.reservationwaiting;

import auth.LoginMember;
import auth.UserDetails;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import nextstep.member.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationWaitingController {
    public final ReservationWaitingService reservationWaitingService;
    public final MemberService memberService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService, MemberService memberService) {
        this.reservationWaitingService = reservationWaitingService;
        this.memberService = memberService;
    }
    @PostMapping("/reservation-waitings")
    public ResponseEntity createReservationWaiting(@LoginMember UserDetails userDetails, @RequestBody ReservationWaitingRequest reservationWaitingRequest) {
        Long id = reservationWaitingService.create(userDetails.getId(), reservationWaitingRequest);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @GetMapping("/reservation-waitings/mine")
    public ResponseEntity readReservationWaitings(@LoginMember UserDetails userDetails) {
        List<ReservationWaiting> results = reservationWaitingService.findAllByMemberId(userDetails.getId());
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservation-waitings/{id}")
    public ResponseEntity deleteReservationWaiting(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        int deletedRowCount = reservationWaitingService.deleteById(userDetails.getId(), id);

        return ResponseEntity.ok().body(Collections.singletonMap("deletedReservationWaitingCount", deletedRowCount));
    }
}
