package nextstep.member;

import auth.LoginMember;
import auth.NeedAuth;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        Long id = memberService.create(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @NeedAuth
    @GetMapping("/me")
    public ResponseEntity me(@LoginMember Long memberId) {
        return ResponseEntity.ok(memberService.findById(memberId));
    }
}
