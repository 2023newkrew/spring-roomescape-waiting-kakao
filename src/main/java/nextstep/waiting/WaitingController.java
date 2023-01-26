package nextstep.waiting;

import auth.AuthenticationException;
import auth.LoginMember;
import auth.TokenMember;
import nextstep.member.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class WaitingController {

    private final WaitingService waitingService;
    private final MemberService memberService;

    public WaitingController(WaitingService waitingService, MemberService memberService) {
        this.waitingService = waitingService;
        this.memberService = memberService;
    }

    @PostMapping("/reservation-waitings")
    public ResponseEntity<Void> createWaiting(@LoginMember TokenMember member, WaitingRequestDTO waitingRequestDTO) {
        if (member == null) {
            throw new AuthenticationException();
        }
        String location = waitingService.create(memberService.findById(member.getId()), waitingRequestDTO);
        return ResponseEntity.created(URI.create(location)).build();
    }
}
