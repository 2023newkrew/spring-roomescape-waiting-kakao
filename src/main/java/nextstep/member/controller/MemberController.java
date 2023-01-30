package nextstep.member.controller;

import lombok.RequiredArgsConstructor;
import nextstep.auth.support.AuthenticationPrincipal;
import nextstep.auth.support.LoginRequired;
import nextstep.member.model.Member;
import nextstep.member.model.MemberRequest;
import nextstep.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> createMember(@Valid @RequestBody MemberRequest memberRequest) {
        Long id = memberService.create(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @LoginRequired
    @GetMapping("/me")
    public ResponseEntity<Member> me(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(member);
    }
}
