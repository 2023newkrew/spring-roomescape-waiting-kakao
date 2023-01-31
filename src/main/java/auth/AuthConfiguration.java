package auth;

import lombok.RequiredArgsConstructor;
import nextstep.member.MemberDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class AuthConfiguration {
    private final MemberDao memberDao;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public UserDetailService userDetailService() {
        return new UserDetailService(memberDao);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProvider(jwtTokenProvider(), userDetailService());
    }

    @Bean
    public LoginController loginController() {
        return new LoginController(authenticationProvider());
    }

    @Bean
    public AdminInterceptor adminInterceptor() {
        return new AdminInterceptor(jwtTokenProvider());
    }

    @Bean
    public LoginMemberArgumentResolver loginMemberArgumentResolver() {
        return new LoginMemberArgumentResolver(authenticationProvider());
    }
}
