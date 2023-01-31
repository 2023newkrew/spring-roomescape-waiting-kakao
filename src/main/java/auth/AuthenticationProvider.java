package auth;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticationProvider {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailService userDetailService;

    public String authenticate(TokenRequest tokenRequest) {
        UserDetail userDetail = userDetailService.getUserDetailByUsername(tokenRequest.getUsername());
        checkAuthentication(userDetail, tokenRequest);

        return jwtTokenProvider.createToken(userDetail.getUsername(), userDetail.getRole());
    }

    private void checkAuthentication(UserDetail userDetail, TokenRequest tokenRequest) {
        if (!userDetail.getPassword().equals(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }
    }
}
