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
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    @PostMapping(path = "", produces = "application/json;charset=utf-8")
    public ResponseEntity<MemberControllerPostResponse> createMember(@Valid @RequestBody MemberControllerPostBody request) {
        var id = service.createMember(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .location(URI.create("/members/" + id))
                             .body(new MemberControllerPostResponse(id));
    }

    @GetMapping(path = "/me", produces = "application/json;charset=utf-8")
    public ResponseEntity<MemberControllerGetResponse> me(@JWTBearerTokenSubject String subject) {
        var member = service.me(Long.parseLong(subject));
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new MemberControllerGetResponse(
                                     member.getId(),
                                     member.getUsername(),
                                     member.getPassword(),
                                     member.getName(),
                                     member.getPhone()
                             ));
    }
}
