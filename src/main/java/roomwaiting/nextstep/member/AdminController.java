package roomwaiting.nextstep.member;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static roomwaiting.support.Messages.UPDATE_ADMIN;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final MemberService memberService;

    public AdminController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> updateAdmin(@RequestBody MemberRequest memberRequest) {
        memberService.updateAdmin(memberRequest);
        return ResponseEntity.ok().body(UPDATE_ADMIN.getMessage() + memberRequest.getName());
    }
}
