package auth.service;

import auth.dto.MemberDetails;
import auth.exception.UnauthenticatedException;
import auth.dto.TokenRequest;
import auth.dto.TokenResponse;
import auth.token.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final MemberDetailsService memberDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberDetailsService memberDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.memberDetailsService = memberDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        MemberDetails memberDetails = memberDetailsService.loadMemberDetailsByUsername(tokenRequest.getUsername());
        if (memberDetails == null || !memberDetails.getPassword().equals(tokenRequest.getPassword())) {
            throw new UnauthenticatedException();
        }

        String accessToken = jwtTokenProvider.createToken(memberDetails.getId() + "", memberDetails.getUsername(), memberDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public MemberDetails extractMemberDetails(String credential) {
        String username = jwtTokenProvider.getUsername(credential);
        return memberDetailsService.loadMemberDetailsByUsername(username);
    }
}
