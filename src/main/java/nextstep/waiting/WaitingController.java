package nextstep.waiting;

import auth.LoginMember;
import auth.UserDetails;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberService;
import nextstep.reservation.ReservationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WaitingController {

    public final WaitingService waitingService;
    public final MemberService memberService;

    @PostMapping("/reservation-waitings")
    public ResponseEntity createWaiting(@LoginMember UserDetails userDetails, @RequestBody WaitingRequest waitingRequest) {
        Member member = memberService.findById(userDetails.getId());
        Long id = waitingService.create(member, waitingRequest);
        
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @GetMapping("/reservations-waitings/mine")
    public ResponseEntity readReservations(@LoginMember UserDetails userDetails) {
        List<WaitingResponse> results = waitingService.findAllByMemberId(userDetails.getId());
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservation-waitings/{id}")
    public ResponseEntity deleteReservation(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        Member member = memberService.findById(userDetails.getId());
        waitingService.deleteById(member, id);

        return ResponseEntity.noContent().build();
    }
}
