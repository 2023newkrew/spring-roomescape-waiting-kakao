package nextstep.presentation;

import auth.domain.userdetails.UserDetails;
import auth.presentation.LoginMember;
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
    public ResponseEntity me(@LoginMember UserDetails userDetails) {
        Member member = memberService.findById(userDetails.getId());
        return ResponseEntity.ok(member);
    }
}
