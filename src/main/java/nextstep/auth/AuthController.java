package nextstep.auth;

import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.member.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/token")
    public ResponseEntity createToken(@RequestBody TokenRequest request) {
        return ResponseEntity.ok(new TokenResponse(authService.createToken(request)));
    }
}
