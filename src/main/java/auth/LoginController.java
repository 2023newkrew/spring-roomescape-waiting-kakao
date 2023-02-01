package auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/login/token")
public class LoginController {
    private final UserDetailsService<?> userDetailsService;

    public LoginController(UserDetailsService<?> userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public ResponseEntity<TokenResponse> loginToken(@RequestBody TokenRequest tokenRequest) {
        TokenResponse token = userDetailsService.createToken(tokenRequest);
        return ResponseEntity.ok(token);
    }
}
