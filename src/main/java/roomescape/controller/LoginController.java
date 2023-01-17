package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LoginControllerTokenPostBody;
import roomescape.dto.LoginControllerTokenPostResponse;
import roomescape.exception.AuthorizationException;
import roomescape.repository.MemberRepository;
import roomescape.service.JWTProvider;

import java.net.URI;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final JWTProvider jwtProvider;
    private final MemberRepository repository;

    public LoginController(JWTProvider jwtProvider, MemberRepository repository) {
        this.jwtProvider = jwtProvider;
        this.repository = repository;
    }


    @PostMapping("/token")
    public ResponseEntity<LoginControllerTokenPostResponse> createToken(@RequestBody LoginControllerTokenPostBody request) {
        var member = repository.selectByUsername(request.getUsername());
        if (!member.getPassword().equals(request.getPassword())) {
            throw new AuthorizationException();
        }
        var token = jwtProvider.createToken(member.getId().toString());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/login/token/" + token))
                .body(new LoginControllerTokenPostResponse(token));
    }
}
