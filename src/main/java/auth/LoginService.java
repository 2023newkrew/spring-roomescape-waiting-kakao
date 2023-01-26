package auth;

import java.util.function.BiFunction;
import java.util.function.Function;

public class LoginService {
    public static class TokenMember{
        private Long id;
        private String role;

        public TokenMember(Long id, String role) {
            this.id = id;
            this.role = role;
        }

        public Long getId() {
            return id;
        }

        public String getRole() {
            return role;
        }
    }
    // private MemberDao memberDao;
    private BiFunction<String, String, TokenMember> findByUsernameCheckPassword;
    private Function<Long, TokenMember> findById;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(BiFunction<String, String, TokenMember> findByUsernameCheckPassword, Function<Long, TokenMember> findById, JwtTokenProvider jwtTokenProvider) {
        this.findByUsernameCheckPassword = findByUsernameCheckPassword;
        this.findById = findById;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        TokenMember member = findByUsernameCheckPassword.apply(tokenRequest.getUsername(), tokenRequest.getPassword());
        if (member == null) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(member.getId() + "", member.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public TokenMember extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return findById.apply(id);
    }
}
