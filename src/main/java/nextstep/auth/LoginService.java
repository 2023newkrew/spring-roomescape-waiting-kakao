package nextstep.auth;

import nextstep.member.Member;
import nextstep.member.MemberDao;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private MemberDetailsService memberDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberDetailsService memberDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.memberDetailsService = memberDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        MemberDetails memberDetails = memberDetailsService.loadMemberDetailsByUsername(tokenRequest.getUsername());
        if (memberDetails == null || !memberDetails.getPassword().equals(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(memberDetails.getId() + "", memberDetails.getUsername(), memberDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public Member extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return memberDao.findById(id);
    }
}
