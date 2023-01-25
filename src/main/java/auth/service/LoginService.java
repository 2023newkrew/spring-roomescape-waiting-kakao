package auth.service;

import auth.support.AuthenticationException;
import auth.support.JwtTokenProvider;
import auth.domain.dto.TokenRequest;
import auth.domain.dto.TokenResponse;
import nextstep.domain.persist.Member;
import nextstep.repository.MemberDao;
import org.springframework.stereotype.Service;

@Service
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

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public Member extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return memberDao.findById(id);
    }
}
