package io.github.aglushkovsky.advertisingservice.util;

import io.github.aglushkovsky.advertisingservice.jwt.JwtAuthentication;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtils {

    public static Long getAuthenticatedUserId() {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return authentication.getId();
    }
}
