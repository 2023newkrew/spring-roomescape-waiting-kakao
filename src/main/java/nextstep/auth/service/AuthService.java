package nextstep.auth.service;

import lombok.RequiredArgsConstructor;
import nextstep.auth.dao.MemberRoleDao;
import nextstep.auth.domain.MemberRoles;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.model.TokenRequest;
import nextstep.auth.model.TokenResponse;
import nextstep.auth.support.JwtTokenProvider;
import nextstep.member.dao.MemberDao;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;
    private final MemberRoleDao memberRoleDao;

    public TokenResponse createToken(TokenRequest tokenRequest) {
        if (!isValidLogin(tokenRequest)) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createCredential(tokenRequest.getMemberName());
        return new TokenResponse(accessToken);
    }

    public MemberRoles findRoleByMemberName(String memberName){
        return memberRoleDao.findByMemberName(memberName);
    }

    private boolean isValidLogin(TokenRequest request) {
        return memberDao.findByMemberName(request.getMemberName())
                .filter(member -> member.isValidPassword(request.getPassword()))
                .isPresent();
    }
}
