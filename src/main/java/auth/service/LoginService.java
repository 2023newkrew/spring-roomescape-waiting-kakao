package auth.service;

import auth.controller.dto.LoginControllerTokenPostBody;
import auth.exception.AuthException;
import errors.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final JWTProvider jwtProvider;
    private final UserAuthentication repository;

    @Transactional(readOnly = true)
    public String createToken(LoginControllerTokenPostBody body) {
        var member = repository.getUser(body.getUsername());

        if (member.isEmpty()) {
            throw new AuthException(ErrorCode.INVALID_LOGIN_INFORMATION);
        }
        if (!member.get().password().equals(body.getPassword())) {
            throw new AuthException(ErrorCode.INVALID_LOGIN_INFORMATION);
        }
        return jwtProvider.createToken(member.get().subject(), member.get().isAdmin());
    }
}
