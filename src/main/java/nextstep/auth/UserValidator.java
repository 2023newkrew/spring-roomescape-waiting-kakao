package nextstep.auth;

import org.springframework.stereotype.Component;

@Component
public interface UserValidator {
    UserDetails validate(String username, String password);
}
