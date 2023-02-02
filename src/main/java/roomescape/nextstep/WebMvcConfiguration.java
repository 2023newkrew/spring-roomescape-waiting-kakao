package roomescape.nextstep;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import roomescape.auth.*;
import roomescape.nextstep.login.LoginService;
import roomescape.nextstep.member.MemberDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberDao memberDao;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    public WebMvcConfiguration(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
 //       registry.addInterceptor(new AdminInterceptor(jwtTokenProvider())).addPathPatterns("/admin/**");
 //   }

    @Bean
    public FilterRegistrationBean<Filter> setFilterRegistration() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>(new AdminFilter(jwtTokenProvider()));
        filterRegistrationBean.addUrlPatterns("/admin/*");
        return filterRegistrationBean;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new LoginMemberArgumentResolver(loginService()));
    }

    @Bean
    public LoginService loginService() {
        return new LoginService(memberDao, jwtTokenProvider());
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey);
    }
}
