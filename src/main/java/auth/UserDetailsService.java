package auth;

public interface UserDetailsService<T extends UserDetails> {

    TokenResponse createToken(TokenRequest tokenRequest);

    T extractMember(String credential);
}
