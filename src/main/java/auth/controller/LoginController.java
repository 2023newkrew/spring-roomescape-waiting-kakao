package auth.controller;

import auth.domain.dto.TokenRequest;
import auth.domain.dto.TokenResponse;
import auth.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
<<<<<<< HEAD
import org.springframework.web.bind.annotation.*;
=======
=======
>>>>>>> a20bcac (refactor: 전반적인 코드 리팩토링)
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
<<<<<<< HEAD
>>>>>>> 72c4f92 (refactor: ConfigurationProperties 사용)
=======
>>>>>>> a20bcac (refactor: 전반적인 코드 리팩토링)

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @Operation(summary = "로그인 API")
    @PostMapping("/token")
    public ResponseEntity<TokenResponse> loginToken(@RequestBody TokenRequest tokenRequest) {
        TokenResponse token = loginService.createToken(tokenRequest);
        return ResponseEntity.ok(token);
    }
}
