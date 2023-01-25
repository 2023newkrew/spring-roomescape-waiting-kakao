package auth.service;

import auth.domain.persist.UserDetails;
import auth.repository.UserDetailsRepository;
import auth.support.AuthenticationException;
import auth.support.JwtTokenProvider;
import auth.domain.dto.TokenRequest;
import auth.domain.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserDetailsRepository userDetailsRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = userDetailsRepository.findByUsername(tokenRequest.getUsername());
        if (userDetails == null || userDetails.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetails extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return userDetailsRepository.findById(id);
    }
}
