package nextstep.member.controller;

import lombok.RequiredArgsConstructor;
import nextstep.auth.resolver.MemberId;
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

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> me(@MemberId Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
