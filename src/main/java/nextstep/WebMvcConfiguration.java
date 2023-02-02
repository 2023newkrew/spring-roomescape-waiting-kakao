package nextstep;

import auth.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableConfigurationProperties(SecurityTokenProperty.class)
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final Environment environment;
    private final UserDetailsDAO userDetailsDAO;
    private final SecurityTokenProperty securityTokenProperty;

    public WebMvcConfiguration(Environment environment, UserDetailsDAO userDetailsDAO, SecurityTokenProperty securityTokenProperty) {
        this.environment = environment;
        this.userDetailsDAO = userDetailsDAO;
        this.securityTokenProperty = securityTokenProperty;
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(){
      return new JwtTokenProvider(
              securityTokenProperty.getSecretKey(),
              Long.parseLong(securityTokenProperty.getExpireLength())
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
