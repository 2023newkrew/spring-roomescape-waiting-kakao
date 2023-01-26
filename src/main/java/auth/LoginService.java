package auth;

import nextstep.member.MemberDao;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;

    private final MemberDao memberDao;

    public LoginService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = memberDao.findByUsername(tokenRequest.getUsername()).toUserDetails();
        if (userDetails == null || userDetails.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetails extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return memberDao.findById(id).toUserDetails();
    }
}
