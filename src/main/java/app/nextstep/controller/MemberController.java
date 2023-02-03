package app.nextstep.controller;

import app.auth.support.LoginUser;
import app.nextstep.domain.Member;
import app.nextstep.dto.MemberRequest;
import app.nextstep.dto.MemberResponse;
import app.nextstep.service.MemberService;
import org.springframework.http.MediaType;
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
        Long id = memberService.create(memberRequest.toMember());
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberResponse> me(@LoginUser Member member) {
        new MemberResponse(memberService.findById(member.getId()));
        return ResponseEntity.ok(new MemberResponse(memberService.findById(member.getId())));
    }
}
