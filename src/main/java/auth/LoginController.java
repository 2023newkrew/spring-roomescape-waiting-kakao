package auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> loginToken(@RequestBody TokenRequest tokenRequest) {
        TokenResponse token = loginService.createToken(tokenRequest);
        return ResponseEntity.ok(token);
    }
}
