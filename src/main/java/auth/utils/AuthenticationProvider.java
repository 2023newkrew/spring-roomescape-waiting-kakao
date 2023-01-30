package auth.utils;

import auth.dto.TokenRequest;
import auth.dto.UserDetails;

public interface AuthenticationProvider {

    UserDetails getUserDetails(TokenRequest tokenRequest);
}
