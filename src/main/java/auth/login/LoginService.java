package auth.login;

import auth.exception.AuthenticationException;
import auth.util.JwtTokenProvider;
import auth.login.dto.TokenRequest;
import auth.login.dto.TokenResponse;

public class LoginService<T extends AbstractMember> {
    private final MemberDao<T> memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberDao<T> memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        T member = memberDao.findByUsername(tokenRequest.getUsername());
        if (member == null || member.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(member.getId() + "", member.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public T extractMember(String credential) {
        Long id = extractPrincipal(credential);
        return memberDao.findById(id);
    }
}
