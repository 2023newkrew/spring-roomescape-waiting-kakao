package com.nextstep.domains.member;

import com.nextstep.domains.member.dtos.MemberRequest;
import com.nextstep.infrastructures.web.UserContext;
import com.authorizationserver.domains.authorization.entities.UserDetailsEntity;
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
    public ResponseEntity me(@UserContext UserDetailsEntity userDetails) {
        return ResponseEntity.ok(userDetails);
    }
}
