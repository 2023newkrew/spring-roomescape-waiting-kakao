package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.annotation.JWTBearerTokenSubject;
import roomescape.dto.MemberControllerGetResponse;
import roomescape.dto.MemberControllerPostBody;
import roomescape.dto.MemberControllerPostResponse;
import roomescape.service.MemberService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    @PostMapping(path = "/api/members", produces = "application/json;charset=utf-8")
    public ResponseEntity<MemberControllerPostResponse> createMember(@Valid @RequestBody MemberControllerPostBody body) {
        var id = service.createMember(body);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .location(URI.create("/api/members/" + id))
                             .body(new MemberControllerPostResponse(id));
    }

    @GetMapping(path = "/api/members/me", produces = "application/json;charset=utf-8")
    public ResponseEntity<MemberControllerGetResponse> me(@JWTBearerTokenSubject String subject) {
        var member = service.me(Long.parseLong(subject));
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
