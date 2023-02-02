package nextstep.member;

import auth.*;
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
        UserDetails userDetails =  memberService.findByUsername(tokenRequest.getUsername()).toUserDetails();
        if(userDetails == null || userDetails.checkWrongPassword(tokenRequest.getPassword())){
            throw new AuthenticationException();
        }

        TokenResponse token = authenticationProvider.createToken(memberService.findByUsername(tokenRequest.getUsername()).toUserDetails());
        return ResponseEntity.ok(token);
    }
}
