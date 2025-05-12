package io.github.aglushkovsky.advertisingservice.annotation;

import io.github.aglushkovsky.advertisingservice.test.config.SecurityTestConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@WebMvcTest
@Import(SecurityTestConfig.class)
@WithMockUser(authorities = "USER")
@Inherited
public @interface WebMvcUnitTest {

    @AliasFor(annotation = WebMvcTest.class, attribute = "value")
    Class<?>[] value();
}
