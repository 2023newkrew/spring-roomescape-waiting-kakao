package nextstep.controller;

import auth.domain.annotation.LoginMember;
import auth.domain.persist.UserDetails;
import lombok.RequiredArgsConstructor;
import nextstep.domain.dto.request.MemberRequest;
import nextstep.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        Long id = memberService.create(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @GetMapping("/me")
    public ResponseEntity me(@LoginMember UserDetails userDetails) {
        return ResponseEntity.ok(userDetails);
    }
}
