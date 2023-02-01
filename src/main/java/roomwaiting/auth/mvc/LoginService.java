package roomwaiting.auth.mvc;

import org.springframework.security.access.AuthorizationServiceException;
import roomwaiting.auth.token.TokenRequest;
import roomwaiting.auth.token.TokenResponse;
import roomwaiting.auth.token.JwtTokenProvider;
import roomwaiting.auth.userdetail.UserDetails;
import roomwaiting.auth.userdetail.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static roomwaiting.support.Messages.*;

@Service
public class LoginService {
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = userDetailsService.findUserDetailsByUsername(tokenRequest.getUsername());
        if (userDetails == null) {
            throw new AuthorizationServiceException(MEMBER_NOT_FOUND.getMessage() + USERNAME + tokenRequest.getUsername());
        }
        if (!passwordEncoder.matches(tokenRequest.getPassword(), userDetails.getPassword())){
            throw new AuthenticationServiceException(PASSWORD_INCORRECT.getMessage());
        }
        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public UserDetails extractMember(String credential) {
        try {
            Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
            return userDetailsService.findUserDetailsById(id);
        } catch (Exception e) {
            throw new AuthenticationServiceException(INVALID_TOKEN.getMessage());
        }
    }
}
