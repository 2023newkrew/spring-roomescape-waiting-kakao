package auth;

import auth.config.LoginMemberDao;
import auth.config.MemberDetails;
import auth.dto.TokenRequest;
import auth.dto.TokenResponse;
import auth.exception.AuthAuthenticationException;
import auth.utils.JwtTokenProvider;
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
            throw new AuthAuthenticationException("토큰이 유효해야 합니다.","invalid token","create Token", LoginService.class.getSimpleName());
        }
        String accessToken = jwtTokenProvider.createToken(memberDetails.getId() + "", memberDetails.getRole());
        return new TokenResponse(accessToken);
    }

    public MemberDetails extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return loginMemberDao.findById(id);
    }
}
