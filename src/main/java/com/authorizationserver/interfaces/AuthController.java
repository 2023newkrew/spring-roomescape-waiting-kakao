package com.authorizationserver.interfaces;

import com.authorizationserver.interfaces.dto.TokenRequest;
import com.authorizationserver.interfaces.dto.TokenResponse;
import com.authorizationserver.domains.authorization.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthorizationService service;

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody @Validated TokenRequest tokenRequest) {
        return ResponseEntity.ok(service.createToken(tokenRequest));
    }
}
