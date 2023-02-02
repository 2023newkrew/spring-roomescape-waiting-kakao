package nextstep;

import auth.AdminInterceptor;
import auth.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nextstep.config.annotation.LoginMemberArgumentResolver;
import nextstep.member.MemberService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(jwtTokenProvider, objectMapper)).addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new LoginMemberArgumentResolver(memberService, jwtTokenProvider));
    }
}
