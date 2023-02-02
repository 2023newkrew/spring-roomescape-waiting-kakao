package nextstep.presentation.front;

import auth.dto.request.LoginMember;
import auth.presentation.argumentresolver.AuthenticationPricipal;
import nextstep.domain.member.Member;
import nextstep.dto.request.MemberRequest;
import nextstep.service.MemberService;
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
    public ResponseEntity me(@AuthenticationPricipal LoginMember loginMember) {
        Member member = memberService.findById(loginMember.getId());
        return ResponseEntity.ok(member);
    }
}
