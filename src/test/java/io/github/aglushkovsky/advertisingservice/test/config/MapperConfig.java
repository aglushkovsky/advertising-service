package io.github.aglushkovsky.advertisingservice.test.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = "io.github.aglushkovsky.advertisingservice.mapper")
public class MapperConfig {
}
