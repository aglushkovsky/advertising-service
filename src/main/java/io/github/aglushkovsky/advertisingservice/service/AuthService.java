package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.impl.UserDao;
import io.github.aglushkovsky.advertisingservice.dto.request.JwtAuthRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.JwtAuthResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.User;
import io.github.aglushkovsky.advertisingservice.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtUtils jwtUtils;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthResponseDto login(JwtAuthRequestDto request) {
        log.info("Start login attempt");

        User user = userDao.findByLogin(request.login())
                .orElseThrow(() -> {
                    log.error("Login attempt failed: login not found");
                    return new UsernameNotFoundException(request.login());
                });

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            log.error("Login attempt failed: wrong password");
            throw new BadCredentialsException("Wrong password");
        }

        String generatedAccessToken = jwtUtils.generateAccessToken(user);

        log.info("Login attempt successful, generated access token");

        return new JwtAuthResponseDto(generatedAccessToken);
    }
}
