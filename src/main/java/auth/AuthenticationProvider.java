package auth;

public interface AuthenticationProvider {

    UserDetails getUserDetails(TokenRequest tokenRequest);
}
