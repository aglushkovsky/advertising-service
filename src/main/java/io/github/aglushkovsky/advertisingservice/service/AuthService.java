package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.impl.UserDao;
import io.github.aglushkovsky.advertisingservice.dto.request.JwtAuthRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.JwtAuthResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.User;
import io.github.aglushkovsky.advertisingservice.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthResponseDto login(JwtAuthRequestDto request) {
        User user = userDao.findByLogin(request.login())
                .orElseThrow(() -> new UsernameNotFoundException(request.login()));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Wrong password");
        }

        String generatedAccessToken = jwtUtils.generateAccessToken(user);
        return new JwtAuthResponseDto(generatedAccessToken);
    }
}
