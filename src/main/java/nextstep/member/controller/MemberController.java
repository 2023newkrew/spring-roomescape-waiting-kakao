package nextstep.member.controller;

import auth.dto.TokenRequest;
import auth.dto.TokenResponse;
import auth.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import nextstep.etc.resolver.LoginUser;
import nextstep.member.domain.Member;
import nextstep.member.dto.MemberRequest;
import nextstep.member.dto.MemberResponse;
import nextstep.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private static final String MEMBER_PATH = "/members";

    private final JwtTokenProvider provider;

    private final MemberService service;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Validated MemberRequest request) {
        MemberResponse member = service.create(request);
        URI location = URI.create(MEMBER_PATH + "/" + member.getId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{member_id}")
    public ResponseEntity<MemberResponse> getById(@PathVariable("member_id") Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Validated TokenRequest tokenRequest) {
        return ResponseEntity.ok(provider.createToken(tokenRequest));
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> me(@LoginUser Member member) {
        return ResponseEntity.ok(service.getById(member.getId()));
    }
}
