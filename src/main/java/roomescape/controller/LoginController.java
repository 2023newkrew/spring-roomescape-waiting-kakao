package roomescape.controller;

import auth.controller.dto.LoginControllerTokenPostBody;
import auth.controller.dto.LoginControllerTokenPostResponse;
import auth.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService service;


    @PostMapping("/token")
    public ResponseEntity<LoginControllerTokenPostResponse> createToken(@Valid @RequestBody LoginControllerTokenPostBody body) {
        var token = service.createToken(body);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/api/login/token/" + token))
                .body(new LoginControllerTokenPostResponse(token));
    }
}
