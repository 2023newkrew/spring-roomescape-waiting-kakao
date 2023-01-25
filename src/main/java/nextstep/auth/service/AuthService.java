package nextstep.auth.service;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.TokenData;
import nextstep.auth.dto.TokenRequest;
import nextstep.auth.dto.TokenResponse;
import nextstep.auth.provider.JwtTokenProvider;
import nextstep.etc.exception.AuthenticationException;
import nextstep.etc.exception.ErrorMessage;
import nextstep.member.domain.MemberRole;
import nextstep.member.dto.MemberResponse;
import nextstep.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final JwtTokenProvider provider;

    private final MemberService memberService;


    public TokenResponse createToken(TokenRequest tokenRequest) {
        MemberResponse member = memberService.getByUsername(tokenRequest.getUsername());
        if (isNullOrWrongPassword(member, tokenRequest.getPassword())) {
            throw new AuthenticationException(ErrorMessage.INVALID_USERNAME_OR_PASSWORD);
        }
        MemberRole role = member.getRole();
        TokenData tokenData = new TokenData(member.getId(), role.toString());

        return new TokenResponse(provider.createToken(tokenData));
    }

    private boolean isNullOrWrongPassword(MemberResponse member, String password) {
        return Objects.isNull(member) || !Objects.equals(member.getPassword(), password);
    }
}
