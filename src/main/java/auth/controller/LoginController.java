package auth.controller;

import auth.domain.dto.request.TokenRequest;
import auth.domain.dto.response.TokenResponse;
import auth.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @Operation(summary = "로그인 API")
    @PostMapping("/token")
    public ResponseEntity<TokenResponse> tokenLogin(@RequestBody TokenRequest tokenRequest) {
        TokenResponse token = loginService.createToken(tokenRequest);
        return ResponseEntity.ok(token);
    }
}
