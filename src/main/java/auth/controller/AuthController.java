package auth.controller;

import auth.dto.TokenRequest;
import auth.dto.TokenResponse;
import auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService service;

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody @Validated TokenRequest tokenRequest) {
        return ResponseEntity.ok(service.createToken(tokenRequest));
    }
}
