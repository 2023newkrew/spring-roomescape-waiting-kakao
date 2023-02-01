package roomescape.controller;

import auth.annotation.JWTMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.MemberControllerGetResponse;
import roomescape.controller.dto.MemberControllerPostBody;
import roomescape.controller.dto.MemberControllerPostResponse;
import roomescape.service.MemberService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    @PostMapping(produces = "application/json;charset=utf-8")
    public ResponseEntity<MemberControllerPostResponse> createMember(@Valid @RequestBody MemberControllerPostBody body) {
        var id = service.createMember(body);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .location(URI.create("/api/members/" + id))
                             .body(new MemberControllerPostResponse(id));
    }

    @GetMapping(path = "/me", produces = "application/json;charset=utf-8")
    public ResponseEntity<MemberControllerGetResponse> me(@JWTMemberId Long memberId) {
        var member = service.me(memberId);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new MemberControllerGetResponse(
                                     member.getId(),
                                     member.getUsername(),
                                     member.getPassword(),
                                     member.getName(),
                                     member.getPhone(),
                                     member.getIsAdmin()
                             ));
    }
}
