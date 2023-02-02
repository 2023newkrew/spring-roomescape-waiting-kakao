package auth;

import nextstep.exception.ErrorCode;
import nextstep.exception.RoomEscapeException;

public class LoginService {
    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(UserDao userDao, JwtTokenProvider jwtTokenProvider) {
        this.userDao = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = userDao.findByUsername(tokenRequest.getUsername())
                .orElseThrow(() -> new RoomEscapeException(ErrorCode.FAILED_TO_LOGIN));
        if (!userDetails.checkPassword(tokenRequest.getPassword())) {
            throw new RoomEscapeException(ErrorCode.FAILED_TO_LOGIN);
        }

        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetails extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return userDao.findById(id)
                .orElseThrow(() -> new RoomEscapeException(ErrorCode.ENTITY_NOT_EXISTS));
    }
}
