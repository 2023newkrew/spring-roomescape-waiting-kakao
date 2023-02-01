package app.auth.service;

import app.auth.domain.User;
import app.auth.dto.TokenResponse;
import app.auth.repository.LoginRepository;
import app.auth.support.AuthenticationException;
import app.auth.util.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private LoginRepository loginRepository;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(LoginRepository loginRepository, JwtTokenProvider jwtTokenProvider) {
        this.loginRepository = loginRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(String username, String password) {
        User user = loginRepository.findByUsername(username);
        if (user == null || user.checkWrongPassword(password)) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(user.getId() + "", user.getRole());

        return new TokenResponse(accessToken);
    }

    public User extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return loginRepository.findById(id);
    }
}
