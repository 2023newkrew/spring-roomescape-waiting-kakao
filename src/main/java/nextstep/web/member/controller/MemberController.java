package nextstep.web.member.controller;

import auth.resolver.LoginMember;
import nextstep.web.member.domain.Member;
import nextstep.web.member.dto.MemberRequest;
import nextstep.web.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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

    @GetMapping("/me")
    public ResponseEntity<Member> me(@LoginMember Member member) {
        return ResponseEntity.ok(member);
    }
}
