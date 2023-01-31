package nextstep.auth.controller;

import lombok.RequiredArgsConstructor;
import nextstep.auth.dto.TokenRequest;
import nextstep.auth.dto.TokenResponse;
import nextstep.auth.service.AuthService;
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
