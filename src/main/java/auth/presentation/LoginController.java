package auth.presentation;

import auth.service.LoginService;
import auth.dto.request.TokenRequest;
import auth.dto.response.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/login")
@ResponseBody
public class LoginController {
    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> loginToken(@RequestBody TokenRequest tokenRequest) {
        TokenResponse token = loginService.createToken(tokenRequest);
        return ResponseEntity.ok(token);
    }
}
