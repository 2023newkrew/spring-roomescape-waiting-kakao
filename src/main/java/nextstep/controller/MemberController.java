package nextstep.controller;

import auth.domain.annotation.LoginMember;
import auth.domain.persist.UserDetails;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "멤버 생성 API")
    @PostMapping
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        Long id = memberService.create(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @Operation(summary = "현재 로그인 된 자신의 정보 조회 API")
    @GetMapping("/me")
    public ResponseEntity me(@LoginMember UserDetails userDetails) {
        return ResponseEntity.ok(userDetails);
    }
}
