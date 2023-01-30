package auth;

import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private UserDetailsDao userDetailsDao;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(UserDetailsDao userDetailsDao, JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsDao = userDetailsDao;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = userDetailsDao.findUserDetailsByUsername(tokenRequest.getUsername());
        if (userDetails == null || userDetails.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException("아이디 혹은 패스워드가 틀렸습니다.");
        }

        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetails extractMember(String credential) {
        if (!jwtTokenProvider.validateToken(credential)) {
            throw new AuthenticationException("유효하지 않은 토큰입니다.");
        }
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        UserDetails userDetails = userDetailsDao.findUserDetailsById(id);
        if (Objects.isNull(userDetails)) {
            throw new AuthenticationException("토큰의 정보가 잘못되었습니다.");
        }
        return userDetails;
    }
}
