package io.github.aglushkovsky.advertisingservice.annotation;

import io.github.aglushkovsky.advertisingservice.test.config.MapperTestConfig;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpringJUnitConfig
@Import({MapperTestConfig.class, ValidationAutoConfiguration.class})
@Inherited
public @interface ServiceUnitTest {

    @AliasFor(annotation = SpringJUnitConfig.class, attribute = "classes")
    Class<?>[] value() default {};
}
