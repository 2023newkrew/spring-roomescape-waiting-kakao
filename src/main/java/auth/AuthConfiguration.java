package auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class AuthConfiguration {
    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailService userDetailService) {
        return new AuthenticationProvider(jwtTokenProvider(), userDetailService);
    }

    @Bean
    public LoginController loginController(AuthenticationProvider authenticationProvider) {
        return new LoginController(authenticationProvider);
    }

    @Bean
    public AdminInterceptor adminInterceptor() {
        return new AdminInterceptor(jwtTokenProvider());
    }

    @Bean
    public LoginMemberArgumentResolver loginMemberArgumentResolver(UserDetailService userDetailService) {
        return new LoginMemberArgumentResolver(jwtTokenProvider(), userDetailService);
    }
}
