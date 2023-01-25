package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LoginControllerTokenPostBody;
import roomescape.dto.LoginControllerTokenPostResponse;
import roomescape.service.LoginService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class LoginController {
    private final LoginService service;


    @PostMapping("/api/login/token")
    public ResponseEntity<LoginControllerTokenPostResponse> createToken(@Valid @RequestBody LoginControllerTokenPostBody body) {
        var token = service.createToken(body);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/api/login/token/" + token))
                .body(new LoginControllerTokenPostResponse(token));
    }
}
