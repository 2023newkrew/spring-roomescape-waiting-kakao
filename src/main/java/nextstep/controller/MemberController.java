package nextstep.controller;

import auth.domain.UserDetails;
import auth.support.template.LoginMember;
import nextstep.controller.dto.request.MemberRequest;
import nextstep.controller.dto.response.MemberResponse;
import nextstep.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody MemberRequest memberRequest) {
        long id = memberService.create(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@LoginMember UserDetails userDetails) {
        return ResponseEntity.ok(
                new MemberResponse(userDetails.getUsername(),
                        userDetails.getName(),
                        userDetails.getPhone(),
                        userDetails.getRole()
                ));
    }
}
