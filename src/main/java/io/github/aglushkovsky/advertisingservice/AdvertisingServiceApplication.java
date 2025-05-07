package io.github.aglushkovsky.advertisingservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        )
)
public class AdvertisingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdvertisingServiceApplication.class, args);
    }

}
