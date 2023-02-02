package roomwaiting.nextstep.member;

import roomwaiting.auth.principal.LoginMember;
import roomwaiting.auth.userdetail.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static roomwaiting.support.Messages.CREATE_USER;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<String> createMember(@RequestBody MemberRequest memberRequest) {
        Long id = memberService.create(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + id)).body(CREATE_USER.getMessage() + memberRequest.getName());
    }

    @GetMapping("/me")
    public ResponseEntity<Member> findMemberOfMine(@LoginMember UserDetails member) {
        return ResponseEntity.ok().body(new Member(member));
    }}
