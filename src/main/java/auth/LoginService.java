package auth;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;

    private final LoginMemberDao loginMemberDao;

    public LoginService(JwtTokenProvider jwtTokenProvider, LoginMemberDao loginMemberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginMemberDao = loginMemberDao;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        MemberDetails memberDetails = loginMemberDao.findByUsername(tokenRequest.getUsername());
        if (memberDetails == null || memberDetails.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(memberDetails.getId() + "", memberDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public MemberDetails extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return loginMemberDao.findById(id);
    }
}
