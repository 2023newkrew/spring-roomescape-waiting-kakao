package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.dto.LoginControllerTokenPostBody;
import roomescape.exception.AuthorizationException;
import roomescape.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final JWTProvider jwtProvider;
    private final MemberRepository repository;

    public String createToken(LoginControllerTokenPostBody body) {
        var member = repository.selectByUsername(body.getUsername());
        if (!member.getPassword().equals(body.getPassword())) {
            throw new AuthorizationException();
        }
        return jwtProvider.createToken(member.getId().toString());
    }
}
