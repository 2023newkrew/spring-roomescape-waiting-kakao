package com.authorizationserver.configurations;

import com.authorizationserver.infrastructures.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AuthMvcConfiguration {

    public AuthMvcConfiguration() {}

    @Bean
    JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }
}
