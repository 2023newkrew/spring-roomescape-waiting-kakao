package nextstep;

import auth.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@PropertySource("classpath:application.properties")
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final Environment environment;
    private final UserDetailsDAO userDetailsDAO;

    public WebMvcConfiguration(Environment environment, UserDetailsDAO userDetailsDAO) {
        this.environment = environment;
        this.userDetailsDAO = userDetailsDAO;
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(){
      return new JwtTokenProvider(
              environment.getProperty("security.jwt.token.secret-key"),
              Long.parseLong(environment.getProperty("security.jwt.token.expire-length"))
      );
    };

    @Bean
    public LoginService loginService(){
        return new LoginService(userDetailsDAO,jwtTokenProvider());
    }

    @Bean
    public LoginController loginController(){
        return new LoginController(loginService());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(jwtTokenProvider())).addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new LoginMemberArgumentResolver(loginService()));
    }
}
