package nextstep;

import auth.*;
import nextstep.member.MemberDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final MemberDao memberDao;

    public WebMvcConfiguration(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(jwtTokenProvider())).addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new LoginMemberArgumentResolver(authenticationProvider()));
    }
}
