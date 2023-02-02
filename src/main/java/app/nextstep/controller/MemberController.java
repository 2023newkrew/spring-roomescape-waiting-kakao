package app.nextstep.controller;

import app.auth.support.LoginUser;
import app.auth.domain.User;
import app.nextstep.dto.MemberRequest;
import app.nextstep.service.MemberService;
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

    @GetMapping("/me")
    public ResponseEntity me(@LoginUser User user) {
        return ResponseEntity.ok(memberService.findById(user.getId()));
    }
}
