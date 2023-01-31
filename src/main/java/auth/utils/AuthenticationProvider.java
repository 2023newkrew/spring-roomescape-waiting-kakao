package auth.utils;

import auth.TokenRequest;
import auth.UserDetails;

public interface AuthenticationProvider {

    UserDetails getUserDetails(TokenRequest tokenRequest);
}
