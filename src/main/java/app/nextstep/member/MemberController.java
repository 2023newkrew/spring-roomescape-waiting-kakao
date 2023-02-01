package app.nextstep.member;

import app.auth.support.AuthenticationPrincipal;
import app.auth.service.UserDetail;
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

    @GetMapping("/me")
    public ResponseEntity me(@AuthenticationPrincipal UserDetail userDetail) {
        return ResponseEntity.ok(memberService.findById(userDetail.getId()));
    }
}
