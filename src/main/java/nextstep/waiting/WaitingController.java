package nextstep.waiting;

import auth.AuthenticationException;
import auth.LoginMember;
import auth.TokenMember;
import nextstep.member.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class WaitingController {

    private final WaitingService waitingService;
    private final MemberService memberService;

    public WaitingController(WaitingService waitingService, MemberService memberService) {
        this.waitingService = waitingService;
        this.memberService = memberService;
    }

    @PostMapping("/reservation-waitings")
    public ResponseEntity<Void> createWaiting(@LoginMember TokenMember member, @RequestBody WaitingRequest waitingRequest) {
        if (member == null) {
            throw new AuthenticationException();
        }
        String location = waitingService.create(memberService.findById(member.getId()), waitingRequest);
        return ResponseEntity.created(URI.create(location)).build();
    }

    @DeleteMapping("/reservation-waitings/{id}")
    public ResponseEntity<Void> deleteWaiting(@LoginMember TokenMember member, @PathVariable Long id) {
        if (member == null) {
            throw new AuthenticationException();
        }
        waitingService.deleteById(memberService.findById(member.getId()), id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/reservation-waitings/mine")
    public ResponseEntity<List<MyWaiting>> readMyReservations(@LoginMember TokenMember member) {
        List<MyWaiting> myReservations = waitingService.findAllByMember(member);
        return ResponseEntity.ok().body(myReservations);
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
