package io.github.aglushkovsky.advertisingservice.util;

import io.github.aglushkovsky.advertisingservice.entity.enumeration.Role;
import io.github.aglushkovsky.advertisingservice.jwt.JwtAuthentication;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@UtilityClass
public class SecurityTestUtils {

    public static void setMockUserInSecurityContext(Long authenticatedUserId) {
        setMockUserInSecurityContext(authenticatedUserId, Role.USER);
    }

    public static void setMockUserInSecurityContext(Long authenticatedUserId, Role role) {
        JwtAuthentication authentication = JwtAuthentication.builder()
                .authenticated(true)
                .id(authenticatedUserId)
                .login("test_user")
                .authorities(List.of(role))
                .build();
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
