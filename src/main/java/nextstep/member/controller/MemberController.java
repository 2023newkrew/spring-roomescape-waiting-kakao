package nextstep.member.controller;

import auth.domain.LoginUser;
import auth.dto.TokenRequest;
import auth.dto.TokenResponse;
import auth.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import nextstep.member.domain.MemberEntity;
import nextstep.member.dto.MemberRequest;
import nextstep.member.dto.MemberResponse;
import nextstep.member.mapper.MemberMapper;
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

    private final MemberMapper mapper;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Validated MemberRequest request) {
        MemberEntity member = service.create(mapper.fromRequest(request));
        URI location = URI.create(MEMBER_PATH + "/" + member.getId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{member_id}")
    public ResponseEntity<MemberResponse> getById(@PathVariable("member_id") Long id) {
        MemberEntity member = service.getById(id);

        return ResponseEntity.ok(mapper.toResponse(member));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Validated TokenRequest tokenRequest) {
        return ResponseEntity.ok(provider.createToken(tokenRequest));
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> me(@LoginUser MemberEntity member) {
        return ResponseEntity.ok(mapper.toResponse(member));
    }
}
