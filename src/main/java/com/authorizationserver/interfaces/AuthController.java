package com.authorizationserver.interfaces;

import com.authorizationserver.domains.authorization.AuthService;
import com.authorizationserver.interfaces.dtos.TokenResponse;
import com.authorizationserver.interfaces.dtos.TokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> loginToken(@RequestBody TokenRequest tokenRequest) {
        TokenResponse token = authService.createToken(tokenRequest);
        return ResponseEntity.ok(token);
    }
}
