package nextstep;

import auth.*;
import nextstep.member.LoginController;
import nextstep.member.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberService memberService;
    private final Environment env;

    public WebMvcConfiguration(MemberService memberService, Environment env) {
        this.memberService = memberService;
        this.env = env;
    }


    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(
                env.getProperty("security.jwt.token.secret-key"),
                env.getProperty("security.jwt.token.expire-length", Long.TYPE)
        );
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProvider(jwtTokenProvider());
    }

    @Bean
    public LoginController LoginController() {
        return new LoginController(authenticationProvider(), memberService);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(jwtTokenProvider())).addPathPatterns("/admin/**");
        registry.addInterceptor(new LoginInterceptor(jwtTokenProvider())).addPathPatterns("/reservations");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new LoginMemberArgumentResolver(authenticationProvider()));
    }
}
