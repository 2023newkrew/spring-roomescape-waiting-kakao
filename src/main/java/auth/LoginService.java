package auth;

import nextstep.member.Member;
import nextstep.member.MemberDao;

public class LoginService {
    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = memberDao.findByUsername(tokenRequest.getUsername());
        if (member == null || member.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(member.getId() + "", member.getRole());

        return new TokenResponse(accessToken);
    }

    public UserDetails extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return new UserDetails(id);
    }
}
