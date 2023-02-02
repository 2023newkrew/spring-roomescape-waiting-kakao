package auth;

import lombok.RequiredArgsConstructor;
import nextstep.member.Member;

@RequiredArgsConstructor
public class AuthenticationProvider {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailService userDetailService;

    public String authenticate(TokenRequest tokenRequest) {
        UserDetail userDetail = userDetailService.getUserDetailByUsername(
                tokenRequest.getUsername()
        );
        if (userDetail == null || !userDetail.getPassword().equals(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }
        return jwtTokenProvider.createToken(userDetail.getId() + "", userDetail.getRole());
    }

    public Member extractMember(String token) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(token));
        return userDetailService.getMemberById(id);
    }
}
