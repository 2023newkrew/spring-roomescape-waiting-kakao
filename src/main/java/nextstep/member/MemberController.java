package nextstep.member;

import auth.config.LoginMember;
import java.net.URI;
import nextstep.exception.MemberAuthorizationWebException;
import nextstep.member.dto.MemberRequest;
import nextstep.member.dto.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest memberRequest) {
        Long id = memberService.create(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<MemberResponse> getById(@LoginMember Member member, @PathVariable Long id) {
        if (!member.getId().equals(id)) {
            throw new MemberAuthorizationWebException("로그인 멤버와 path의 id는 같아야 합니다.", id.toString(),
                    "member 정보 조회");
        }
        return ResponseEntity.ok(MemberResponse.of(member));
    }
}
