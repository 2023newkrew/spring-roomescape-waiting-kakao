package nextstep.member;

import auth.AuthenticationProvider;
import auth.TokenRequest;
import auth.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping
@ResponseBody
public class LoginController {
    private AuthenticationProvider authenticationProvider;
    private MemberService memberService;

    public LoginController(AuthenticationProvider authenticationProvider, MemberService memberService) {
        this.authenticationProvider = authenticationProvider;
        this.memberService = memberService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> loginToken(@RequestBody TokenRequest tokenRequest) {
        TokenResponse token = authenticationProvider.createToken(
                memberService.findByUsername(tokenRequest.getUsername()).toUserDetails(),
                tokenRequest.getPassword()
        );
        return ResponseEntity.ok(token);
    }
}
