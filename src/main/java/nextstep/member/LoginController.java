package nextstep.member;

import auth.AuthenticationProvider;
import auth.TokenRequest;
import auth.TokenResponse;
import auth.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping
@ResponseBody
public class LoginController {
    private final AuthenticationProvider authenticationProvider;
    private final MemberService memberService;

    public LoginController(AuthenticationProvider authenticationProvider, MemberService memberService) {
        this.authenticationProvider = authenticationProvider;
        this.memberService = memberService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> loginToken(@RequestBody TokenRequest tokenRequest) {
        UserDetails userDetails = memberService.findByUsername(tokenRequest.getUsername()).toUserDetails();
        TokenResponse token = authenticationProvider.createToken(
                userDetails,
                tokenRequest.getPassword()
        );
        return ResponseEntity.ok(token);
    }
}
