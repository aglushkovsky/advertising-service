package io.github.aglushkovsky.advertisingservice.util;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Named("MappingUtils")
public class MappingUtils {

    private final PasswordEncoder passwordEncoder;

    @Named("getPasswordHash")
    public String getPasswordHash(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
