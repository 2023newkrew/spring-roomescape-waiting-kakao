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
//        Member member = memberDao.findByUsername(tokenRequest.getUsername());
//        if (member == null || member.checkWrongPassword(tokenRequest.getPassword())) {
//            throw new AuthenticationException();
//        }
        UserDetails userDetails = null;
        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetails extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        String role = jwtTokenProvider.getRole(credential);
        return new UserDetails(id, role);
    }
}
