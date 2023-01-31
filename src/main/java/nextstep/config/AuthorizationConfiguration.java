package nextstep.config;

import auth.config.AuthConfiguration;
import auth.login.MemberDao;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthorizationConfiguration extends AuthConfiguration {
    public AuthorizationConfiguration(MemberDao memberDao) {
        super(memberDao);
    }
}
