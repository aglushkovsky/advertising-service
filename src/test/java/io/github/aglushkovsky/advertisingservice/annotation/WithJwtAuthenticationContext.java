package io.github.aglushkovsky.advertisingservice.annotation;

import io.github.aglushkovsky.advertisingservice.util.WithJwtAuthenticationContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@WithSecurityContext(factory = WithJwtAuthenticationContextFactory.class)
public @interface WithJwtAuthenticationContext {
    long id() default 1L;
    String username() default "test_user";
    String[] authorities() default {"USER"};
}
