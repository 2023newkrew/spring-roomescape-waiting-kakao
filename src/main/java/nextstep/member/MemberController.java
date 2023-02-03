package nextstep.member;

import auth.LoginMember;
import auth.login.UserDetails;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity createMember(@RequestBody final MemberRequest memberRequest) {
        Long id = memberService.create(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + id))
            .build();
    }

    @GetMapping("/me")
    public ResponseEntity me(@LoginMember final UserDetails userDetails) {
        Member member = memberService.findById(userDetails.getId());
        return ResponseEntity.ok(member);
    }
}
