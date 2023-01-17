package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.MemberControllerGetResponse;
import roomescape.dto.MemberControllerPostBody;
import roomescape.dto.MemberControllerPostResponse;
import roomescape.repository.MemberRepository;
import roomescape.resolver.JWTBearerTokenSubject;

import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository repository;

    public MemberController(MemberRepository repository) {
        this.repository = repository;
    }

    @PostMapping(path = "", produces = "application/json;charset=utf-8")
    public ResponseEntity<MemberControllerPostResponse> createMember(@RequestBody MemberControllerPostBody req) {
        var id = repository.insert(req.getUsername(), req.getPassword(), req.getName(), req.getPhone());
        return ResponseEntity.status(HttpStatus.CREATED)
                             .location(URI.create("/members/" + id))
                             .body(new MemberControllerPostResponse(id));
    }

    @GetMapping(path = "/me", produces = "application/json;charset=utf-8")
    public ResponseEntity<MemberControllerGetResponse> me(@JWTBearerTokenSubject String subject) {
        var member = repository.selectById(Long.parseLong(subject));
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
