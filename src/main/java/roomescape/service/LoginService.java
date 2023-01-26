package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.LoginControllerTokenPostBody;
import roomescape.controller.errors.ErrorCode;
import roomescape.repository.MemberRepository;
import roomescape.service.exception.ServiceException;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final JWTProvider jwtProvider;
    private final MemberRepository repository;

    public String createToken(LoginControllerTokenPostBody body) {
        var member = repository.selectByUsername(body.getUsername());
        if (!member.getPassword().equals(body.getPassword())) {
            throw new ServiceException(ErrorCode.INVALID_LOGIN_INFORMATION);
        }
        return jwtProvider.createToken(member.getId().toString(), member.getIsAdmin());
    }
}
