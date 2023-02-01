package app.auth.service;

import app.auth.dao.LoginDao;
import app.auth.domain.UserDetail;
import app.auth.dto.TokenResponse;
import app.auth.support.AuthenticationException;
import app.auth.util.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private LoginDao loginDao;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(LoginDao loginDao, JwtTokenProvider jwtTokenProvider) {
        this.loginDao = loginDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(String username, String password) {
        UserDetail userDetail = loginDao.findByUsername(username);
        if (userDetail == null || userDetail.checkWrongPassword(password)) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(userDetail.getId() + "", userDetail.getRole());

        return new TokenResponse(accessToken);
    }

    public UserDetail extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return loginDao.findById(id);
    }
}
