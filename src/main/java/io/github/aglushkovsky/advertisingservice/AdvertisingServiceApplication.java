package io.github.aglushkovsky.advertisingservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.*;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.*;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Advertising Service",
                version = "v1",
                description = "REST API for advertising service",
                contact = @Contact(
                        name = "Alexander",
                        url = "https://github.io/aglushkovsky"
                )
        ),
        security = @SecurityRequirement(name = "jwtAuth")
)
@SecurityScheme(
        name = "jwtAuth",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = HEADER
)
public class AdvertisingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdvertisingServiceApplication.class, args);
    }

}
