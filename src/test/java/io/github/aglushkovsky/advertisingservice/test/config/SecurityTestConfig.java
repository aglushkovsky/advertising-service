package io.github.aglushkovsky.advertisingservice.test.config;

import io.github.aglushkovsky.advertisingservice.config.SecurityConfig;
import io.github.aglushkovsky.advertisingservice.jwt.JwtUtils;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(SecurityConfig.class)
public class SecurityTestConfig {

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }
}
