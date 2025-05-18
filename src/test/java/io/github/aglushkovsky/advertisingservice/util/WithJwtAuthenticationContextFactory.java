package io.github.aglushkovsky.advertisingservice.util;

import io.github.aglushkovsky.advertisingservice.annotation.WithJwtAuthenticationContext;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.Role;
import io.github.aglushkovsky.advertisingservice.jwt.JwtAuthentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.List;

public class WithJwtAuthenticationContextFactory implements WithSecurityContextFactory<WithJwtAuthenticationContext> {

    @Override
    public SecurityContext createSecurityContext(WithJwtAuthenticationContext annotation) {
        JwtAuthentication authentication = JwtAuthentication.builder()
                .authenticated(true)
                .id(annotation.id())
                .login(annotation.username())
                .authorities(List.of(Arrays.stream(annotation.roles()).map(Role::valueOf).toArray(Role[]::new)))
                .build();
        authentication.setAuthenticated(true);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return context;
    }
}
